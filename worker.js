const PASSWORD_ITERATIONS = 100000;
const SESSION_DAYS = 30;

function json(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Cache-Control": "no-store",
      "X-Content-Type-Options": "nosniff"
    }
  });
}

function fail(message, status) {
  return json({ success: false, message }, status);
}

function bytesToBase64Url(bytes) {
  let binary = "";
  for (const byte of bytes) binary += String.fromCharCode(byte);

  return btoa(binary)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/g, "");
}

function base64UrlToBytes(value) {
  const base64 = value
    .replace(/-/g, "+")
    .replace(/_/g, "/");

  const binary = atob(
    base64 + "=".repeat((4 - base64.length % 4) % 4)
  );

  return Uint8Array.from(binary, c => c.charCodeAt(0));
}

function randomToken() {
  return bytesToBase64Url(crypto.getRandomValues(new Uint8Array(32)));
}

async function sha256(value) {
  const digest = await crypto.subtle.digest(
    "SHA-256",
    new TextEncoder().encode(value)
  );

  return bytesToBase64Url(new Uint8Array(digest));
}

async function derivePassword(password, salt) {
  const key = await crypto.subtle.importKey(
    "raw",
    new TextEncoder().encode(password),
    "PBKDF2",
    false,
    ["deriveBits"]
  );

  const bits = await crypto.subtle.deriveBits(
    {
      name: "PBKDF2",
      salt,
      iterations: PASSWORD_ITERATIONS,
      hash: "SHA-256"
    },
    key,
    256
  );

  return bytesToBase64Url(new Uint8Array(bits));
}

async function passwordMatches(password, saltText, expectedHash) {
  const actual = await derivePassword(
    password,
    base64UrlToBytes(saltText)
  );

  const a = new TextEncoder().encode(actual);
  const b = new TextEncoder().encode(expectedHash);

  if (a.length !== b.length) return false;

  let difference = 0;
  for (let i = 0; i < a.length; i++) {
    difference |= a[i] ^ b[i];
  }

  return difference === 0;
}

function validPassword(password) {
  return typeof password === "string"
    && password.length >= 12
    && password.length <= 128
    && !/[\s\u0000-\u001f\u007f]/.test(password);
}

async function readJson(request) {
  const type = request.headers.get("Content-Type") || "";

  if (!type.toLowerCase().includes("application/json")) {
    throw new Error("JSON_REQUIRED");
  }

  const length = Number(request.headers.get("Content-Length") || 0);
  if (length > 12000) throw new Error("BODY_TOO_LARGE");

  return await request.json();
}

async function register(request, env) {
  let body;

  try {
    body = await readJson(request);
  } catch (error) {
    if (error.message === "BODY_TOO_LARGE") {
      return fail("Request is too large.", 413);
    }
    return fail("A valid JSON request is required.", 400);
  }

  const firstName = typeof body.firstName === "string"
    ? body.firstName.trim() : "";
  const lastName = typeof body.lastName === "string"
    ? body.lastName.trim() : "";
  const username = typeof body.username === "string"
    ? body.username.trim().toLowerCase() : "";
  const country = typeof body.country === "string"
    ? body.country.trim() : "";
  const password = body.password;

  if (firstName.length < 2 || firstName.length > 80) {
    return fail("Enter a valid first name.", 400);
  }

  if (lastName.length < 2 || lastName.length > 80) {
    return fail("Enter a valid last name.", 400);
  }

  if (!/^[a-z0-9_]{3,20}$/.test(username)) {
    return fail("Username must contain 3–20 letters, numbers or underscores.", 400);
  }

  if (country.length < 2 || country.length > 80) {
    return fail("Enter a valid country.", 400);
  }

  if (!validPassword(password)) {
    return fail("Password must be 12–128 characters without whitespace.", 400);
  }

  const existing = await env.DB.prepare(
    "SELECT id FROM users WHERE username = ?"
  ).bind(username).first();

  if (existing) {
    return fail("This username is already registered.", 409);
  }

  const userId = crypto.randomUUID();
  const salt = crypto.getRandomValues(new Uint8Array(16));
  const saltText = bytesToBase64Url(salt);
  const passwordHash = await derivePassword(password, salt);
  const now = Math.floor(Date.now() / 1000);

  try {
    await env.DB.prepare(`
      INSERT INTO users
        (id, first_name, last_name, username, country,
         password_hash, password_salt, created_at)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `).bind(
      userId,
      firstName,
      lastName,
      username,
      country,
      passwordHash,
      saltText,
      now
    ).run();
  } catch {
    return fail("Could not create the account. The username may already exist.", 409);
  }

  return await createSession(env, userId, {
    id: userId,
    firstName,
    lastName,
    username,
    country
  });
}

async function login(request, env) {
  let body;

  try {
    body = await readJson(request);
  } catch (error) {
    if (error.message === "BODY_TOO_LARGE") {
      return fail("Request is too large.", 413);
    }
    return fail("A valid JSON request is required.", 400);
  }

  const username = typeof body.username === "string"
    ? body.username.trim().toLowerCase() : "";
  const password = body.password;

  if (!/^[a-z0-9_]{3,20}$/.test(username)
      || typeof password !== "string"
      || password.length > 128) {
    return fail("Invalid username or password.", 401);
  }

  const user = await env.DB.prepare(`
    SELECT id, first_name, last_name, username, country,
           password_hash, password_salt
    FROM users
    WHERE username = ?
  `).bind(username).first();

  if (!user) return fail("Invalid username or password.", 401);

  const matches = await passwordMatches(
    password,
    user.password_salt,
    user.password_hash
  );

  if (!matches) return fail("Invalid username or password.", 401);

  return await createSession(env, user.id, {
    id: user.id,
    firstName: user.first_name,
    lastName: user.last_name,
    username: user.username,
    country: user.country
  });
}

async function createSession(env, userId, account) {
  const token = randomToken();
  const tokenHash = await sha256(token);
  const now = Math.floor(Date.now() / 1000);
  const expiresAt = now + SESSION_DAYS * 24 * 60 * 60;

  await env.DB.prepare(`
    INSERT INTO sessions (token_hash, user_id, expires_at, created_at)
    VALUES (?, ?, ?, ?)
  `).bind(tokenHash, userId, expiresAt, now).run();

  return json({
    success: true,
    message: "Account authenticated successfully.",
    token,
    expiresAt,
    account
  });
}

async function getCurrentAccount(request, env) {
  const authorization = request.headers.get("Authorization") || "";
  const match = authorization.match(/^Bearer ([A-Za-z0-9_-]{40,60})$/);

  if (!match) return fail("Authentication required.", 401);

  const tokenHash = await sha256(match[1]);
  const now = Math.floor(Date.now() / 1000);

  const row = await env.DB.prepare(`
    SELECT u.id, u.first_name, u.last_name, u.username, u.country
    FROM sessions s
    JOIN users u ON u.id = s.user_id
    WHERE s.token_hash = ? AND s.expires_at > ?
  `).bind(tokenHash, now).first();

  if (!row) return fail("Session expired or invalid.", 401);

  return json({
    success: true,
    account: {
      id: row.id,
      firstName: row.first_name,
      lastName: row.last_name,
      username: row.username,
      country: row.country
    }
  });
}

async function logout(request, env) {
  const authorization = request.headers.get("Authorization") || "";
  const match = authorization.match(/^Bearer ([A-Za-z0-9_-]{40,60})$/);

  if (!match) return fail("Authentication required.", 401);

  const tokenHash = await sha256(match[1]);

  await env.DB.prepare(
    "DELETE FROM sessions WHERE token_hash = ?"
  ).bind(tokenHash).run();

  return json({ success: true, message: "Logged out." });
}

export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    const path = url.pathname.replace(/\/+$/, "") || "/";

    if (request.method === "GET" && path === "/") {
      return json({
        app: "Horus",
        service: "Horus Maps API",
        status: "online"
      });
    }

    if (request.method === "POST" && path === "/api/register") {
      return register(request, env);
    }

    if (request.method === "POST" && path === "/api/login") {
      return login(request, env);
    }

    if (request.method === "GET" && path === "/api/me") {
      return getCurrentAccount(request, env);
    }

    if (request.method === "POST" && path === "/api/logout") {
      return logout(request, env);
    }

    return fail("Endpoint not found.", 404);
  }
};
