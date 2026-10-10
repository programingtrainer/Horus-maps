package com.horus.app;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.*;
import android.graphics.drawable.GradientDrawable;

import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.view.animation.*;

import android.widget.*;

import java.security.MessageDigest;
import java.util.Locale;

public class MainActivity extends Activity {

    private FrameLayout root;
    private MapBackground background;

    private Handler handler =
            new Handler(Looper.getMainLooper());

    private int blue = Color.rgb(20, 105, 190);
    private int cyan = Color.rgb(35, 190, 210);
    private int dark = Color.rgb(20, 38, 58);
    private int gray = Color.rgb(95, 115, 135);

    private SharedPreferences accountStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountStore = getSharedPreferences(
                "horus_account",
                MODE_PRIVATE
        );

        showSplash();
    }

    // ============================================================
    // SPLASH
    // ============================================================

    private void showSplash() {

        root = new FrameLayout(this);

        background = new MapBackground(this);
        root.addView(background);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setGravity(Gravity.CENTER);

        content.setPadding(
                dp(30),
                dp(20),
                dp(30),
                dp(20)
        );

        FrameLayout logoCard =
                createLogoCard(112);

        TextView title = text(
                "HORUS MAPS",
                29,
                Color.rgb(20, 45, 70),
                Typeface.BOLD
        );

        title.setLetterSpacing(0.12f);

        TextView subtitle = text(
                "BUILDING THE EARTH MODEL",
                11,
                gray,
                Typeface.NORMAL
        );

        subtitle.setLetterSpacing(0.16f);

        content.addView(
                logoCard,
                new LinearLayout.LayoutParams(
                        dp(112),
                        dp(112)
                )
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-2, -2);

        titleParams.topMargin = dp(24);

        content.addView(
                title,
                titleParams
        );

        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(-2, -2);

        subParams.topMargin = dp(9);

        content.addView(
                subtitle,
                subParams
        );

        root.addView(
                content,
                new FrameLayout.LayoutParams(-1, -1)
        );

        setContentView(root);

        logoCard.setScaleX(0.65f);
        logoCard.setScaleY(0.65f);
        logoCard.setAlpha(0f);

        title.setAlpha(0f);
        subtitle.setAlpha(0f);

        logoCard.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(850)
                .setInterpolator(
                        new OvershootInterpolator(1.15f)
                )
                .start();

        title.animate()
                .alpha(1f)
                .setStartDelay(550)
                .setDuration(650)
                .start();

        subtitle.animate()
                .alpha(1f)
                .setStartDelay(850)
                .setDuration(650)
                .start();

        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        boolean hasSavedAccount =
                                accountStore.getBoolean("logged_in", false)
                                && accountStore.contains("username")
                                && accountStore.contains("password_hash");

                        if (hasSavedAccount) {
                            showDashboard();
                        } else {
                            showWelcome();
                        }
                    }
                },
                2600
        );
    }

    // ============================================================
    // WELCOME
    // ============================================================

    private void showWelcome() {

        root.removeAllViews();

        background =
                new MapBackground(this);

        root.addView(background);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setPadding(
                dp(26),
                dp(28),
                dp(26),
                dp(25)
        );

        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        FrameLayout miniLogo =
                createLogoCard(52);

        TextView brand = text(
                "HORUS",
                20,
                dark,
                Typeface.BOLD
        );

        brand.setLetterSpacing(0.10f);

        LinearLayout brandBox =
                new LinearLayout(this);

        brandBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        brandBox.addView(
                miniLogo,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(52)
                )
        );

        LinearLayout.LayoutParams brandTextParams =
                new LinearLayout.LayoutParams(-2, -2);

        brandTextParams.leftMargin =
                dp(13);

        brandBox.addView(
                brand,
                brandTextParams
        );

        top.addView(
                brandBox,
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
                        1f
                )
        );

        TextView version =
                text(
                        "PREVIEW",
                        10,
                        blue,
                        Typeface.BOLD
                );

        version.setBackground(
                rounded(
                        Color.argb(24, 20, 105, 190),
                        30
                )
        );

        version.setPadding(
                dp(13),
                dp(7),
                dp(13),
                dp(7)
        );

        top.addView(version);

        page.addView(top);

        Space topSpace =
                new Space(this);

        page.addView(
                topSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.65f
                )
        );

        TextView eyebrow =
                text(
                        "A NEW WAY TO SEE EARTH",
                        11,
                        blue,
                        Typeface.BOLD
                );

        eyebrow.setLetterSpacing(0.15f);

        page.addView(eyebrow);

        TextView headline =
                text(
                        "Explore the world.\nPowered by Horus.",
                        37,
                        dark,
                        Typeface.BOLD
                );

        headline.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams headlineParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        headlineParams.topMargin =
                dp(13);

        page.addView(
                headline,
                headlineParams
        );

        TextView description =
                text(
                        "Discover places, understand geography, " +
                        "and experience a new generation of digital maps.",
                        16,
                        gray,
                        Typeface.NORMAL
                );

        description.setLineSpacing(
                dp(4),
                1.0f
        );

        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        descParams.topMargin =
                dp(18);

        page.addView(
                description,
                descParams
        );

        LinearLayout features =
                new LinearLayout(this);

        features.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addFeature(
                features,
                "EARTH",
                "One model"
        );

        addFeature(
                features,
                "DATA",
                "Verified"
        );

        addFeature(
                features,
                "MAP",
                "Connected"
        );

        LinearLayout.LayoutParams featureParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                );

        featureParams.topMargin =
                dp(28);

        page.addView(
                features,
                featureParams
        );

        Space bottomSpace =
                new Space(this);

        page.addView(
                bottomSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        1f
                )
        );

        TextView start =
                button(
                        "START NOW  〉",
                        17
                );

        LinearLayout.LayoutParams startParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(64)
                );

        startParams.bottomMargin =
                dp(14);

        page.addView(
                start,
                startParams
        );

        TextView footer =
                text(
                        "HORUS MAPS  •  BUILDING THE EARTH MODEL",
                        9,
                        Color.rgb(135, 150, 165),
                        Typeface.BOLD
                );

        footer.setGravity(
                Gravity.CENTER
        );

        footer.setLetterSpacing(0.08f);

        page.addView(
                footer,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(22)
                )
        );

        root.addView(
                page,
                new FrameLayout.LayoutParams(-1, -1)
        );

        page.setAlpha(0f);
        page.setTranslationY(dp(35));

        page.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(850)
                .setInterpolator(
                        new DecelerateInterpolator()
                )
                .start();

        start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pressAnimation(v);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showAuthChoice();
                                    }
                                },
                                180
                        );
                    }
                }
        );
    }

    // ============================================================
    // AUTH CHOICE
    // ============================================================

    private void showAuthChoice() {

        root.removeAllViews();

        background =
                new MapBackground(this);

        root.addView(background);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setPadding(
                dp(26),
                dp(28),
                dp(26),
                dp(25)
        );

        TextView back =
                text(
                        "‹   BACK",
                        13,
                        blue,
                        Typeface.BOLD
                );

        back.setGravity(
                Gravity.CENTER_VERTICAL
        );

        page.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pressAnimation(v);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showWelcome();
                                    }
                                },
                                140
                        );
                    }
                }
        );

        Space space1 =
                new Space(this);

        page.addView(
                space1,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.55f
                )
        );

        FrameLayout logo =
                createLogoCard(88);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        dp(88),
                        dp(88)
                );

        logoParams.gravity =
                Gravity.CENTER_HORIZONTAL;

        page.addView(
                logo,
                logoParams
        );

        TextView title =
                text(
                        "Welcome to Horus",
                        31,
                        dark,
                        Typeface.BOLD
                );

        title.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        titleParams.topMargin =
                dp(24);

        page.addView(
                title,
                titleParams
        );

        TextView subtitle =
                text(
                        "Your journey starts here.",
                        16,
                        gray,
                        Typeface.NORMAL
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        subParams.topMargin =
                dp(9);

        page.addView(
                subtitle,
                subParams
        );

        Space space2 =
                new Space(this);

        page.addView(
                space2,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.42f
                )
        );

        TextView create =
                button(
                        "CREATE ACCOUNT",
                        16
                );

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                );

        createParams.bottomMargin =
                dp(13);

        page.addView(
                create,
                createParams
        );

        TextView login =
                outlineButton(
                        "LOGIN",
                        16
                );

        page.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView info =
                text(
                        "Create your own Horus identity and start exploring.",
                        11,
                        Color.rgb(135, 150, 165),
                        Typeface.NORMAL
                );

        info.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                );

        infoParams.topMargin =
                dp(12);

        page.addView(
                info,
                infoParams
        );

        root.addView(
                page,
                new FrameLayout.LayoutParams(-1, -1)
        );

        page.setAlpha(0f);
        page.setTranslationX(dp(45));

        page.animate()
                .alpha(1f)
                .translationX(0)
                .setDuration(700)
                .setInterpolator(
                        new DecelerateInterpolator()
                )
                .start();

        create.setScaleX(0.92f);
        create.setScaleY(0.92f);
        create.setAlpha(0f);

        login.setScaleX(0.92f);
        login.setScaleY(0.92f);
        login.setAlpha(0f);

        create.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(420)
                .setDuration(500)
                .setInterpolator(
                        new OvershootInterpolator(1.1f)
                )
                .start();

        login.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(530)
                .setDuration(500)
                .setInterpolator(
                        new OvershootInterpolator(1.1f)
                )
                .start();

        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pressAnimation(v);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showCreateAccount();
                                    }
                                },
                                150
                        );
                    }
                }
        );

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressAnimation(v);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showLogin();
                                    }
                                },
                                150
                        );
                    }
                }
        );
    }

    // ============================================================
    // CREATE ACCOUNT
    // ============================================================

    private void showCreateAccount() {

        root.removeAllViews();

        background = new MapBackground(this);
        root.addView(background);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(25), dp(24), dp(25), dp(30));

        scroll.addView(page);

        TextView back = text(
                "‹   CREATE ACCOUNT",
                13,
                blue,
                Typeface.BOLD
        );

        page.addView(
                back,
                new LinearLayout.LayoutParams(-1, dp(45))
        );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressAnimation(v);
                showAuthChoice();
            }
        });

        FrameLayout logo = createLogoCard(66);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(dp(66), dp(66));

        logoParams.gravity = Gravity.CENTER_HORIZONTAL;

        page.addView(logo, logoParams);

        TextView title = text(
                "Create your Horus account",
                28,
                dark,
                Typeface.BOLD
        );

        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);

        titleParams.topMargin = dp(18);

        page.addView(title, titleParams);

        TextView subtitle = text(
                "Create your identity inside the Horus ecosystem.",
                14,
                gray,
                Typeface.NORMAL
        );

        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(-1, -2);

        subtitleParams.topMargin = dp(8);

        page.addView(subtitle, subtitleParams);

        TextView firstName = input("First name", false);
        TextView lastName = input("Last name", false);
        TextView username = input("Username", false);

        addField(page, "FIRST NAME", firstName);
        addField(page, "LAST NAME", lastName);
        addField(page, "HORUS USERNAME", username);

        final EditText password =
                addPasswordField(
                        page,
                        "PASSWORD",
                        "Create a strong password"
                );

        final EditText confirmPassword =
                addPasswordField(
                        page,
                        "CONFIRM PASSWORD",
                        "Enter your password again"
                );

        final EditText country =
                (EditText) input(
                        "Select your country",
                        false
                );

        country.setFocusable(false);
        country.setClickable(true);
        country.setCursorVisible(false);

        country.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                android.R.drawable.arrow_down_float,
                0
        );

        addField(page, "COUNTRY", country);

        country.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCountryPicker(country);
                    }
                }
        );

        TextView passwordInfo = text(
                "Password requirements:\n" +
                "• At least 12 characters\n" +
                "• One uppercase letter (A–Z)\n" +
                "• One lowercase letter (a–z)\n" +
                "• One number (0–9)\n" +
                "• One special character (! @ # $ % ...)\n" +
                "• No spaces",
                12,
                gray,
                Typeface.NORMAL
        );

        passwordInfo.setPadding(
                dp(5),
                dp(5),
                dp(5),
                dp(5)
        );

        LinearLayout.LayoutParams passwordInfoParams =
                new LinearLayout.LayoutParams(-1, -2);

        passwordInfoParams.topMargin = dp(5);

        page.addView(passwordInfo, passwordInfoParams);

        password.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (hasFocus) {
                            passwordInfo.setTextColor(blue);
                        } else {
                            passwordInfo.setTextColor(gray);
                        }
                    }
                }
        );

        TextView identity = text(
                "Your standard Horus identity will be:\nusername@horus.com",
                12,
                gray,
                Typeface.NORMAL
        );

        identity.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams identityParams =
                new LinearLayout.LayoutParams(-1, -2);

        identityParams.topMargin = dp(18);

        page.addView(identity, identityParams);

        TextView legalIntro = text(
                "Before creating your account, please review the Horus Terms of Use and Privacy Policy.",
                12,
                gray,
                Typeface.NORMAL
        );

        legalIntro.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams legalIntroParams =
                new LinearLayout.LayoutParams(-1, -2);

        legalIntroParams.topMargin = dp(18);

        page.addView(legalIntro, legalIntroParams);

        LinearLayout legalLinks =
                new LinearLayout(this);

        legalLinks.setGravity(Gravity.CENTER);
        legalLinks.setOrientation(LinearLayout.HORIZONTAL);

        TextView termsLink = text(
                "Terms of Use",
                13,
                blue,
                Typeface.BOLD
        );

        TextView separator = text(
                "   •   ",
                13,
                gray,
                Typeface.NORMAL
        );

        TextView privacyLink = text(
                "Privacy Policy",
                13,
                blue,
                Typeface.BOLD
        );

        legalLinks.addView(termsLink);
        legalLinks.addView(separator);
        legalLinks.addView(privacyLink);

        LinearLayout.LayoutParams legalLinksParams =
                new LinearLayout.LayoutParams(-1, dp(42));

        page.addView(legalLinks, legalLinksParams);

        termsLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressAnimation(v);
                        showLegalPage("TERMS OF USE");
                    }
                }
        );

        privacyLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressAnimation(v);
                        showLegalPage("PRIVACY POLICY");
                    }
                }
        );

        final CheckBox terms =
                new CheckBox(this);

        terms.setText(
                "I agree to the Terms of Use and Privacy Policy"
        );

        terms.setTextSize(12);
        terms.setTextColor(gray);
        terms.setPadding(0, 0, 0, 0);

        LinearLayout.LayoutParams termsParams =
                new LinearLayout.LayoutParams(-1, dp(55));

        page.addView(terms, termsParams);

        TextView status = text(
                "",
                12,
                Color.rgb(210, 60, 70),
                Typeface.BOLD
        );

        status.setGravity(Gravity.CENTER);

        page.addView(
                status,
                new LinearLayout.LayoutParams(-1, dp(42))
        );

        TextView create =
                button(
                        "CREATE MY HORUS ACCOUNT",
                        15
                );

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(-1, dp(62));

        createParams.topMargin = dp(5);

        page.addView(create, createParams);

        root.addView(
                scroll,
                new FrameLayout.LayoutParams(-1, -1)
        );

        scroll.setAlpha(0f);
        scroll.setTranslationY(dp(35));

        scroll.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(650)
                .setInterpolator(
                        new DecelerateInterpolator()
                )
                .start();

        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String first =
                                ((EditText) firstName)
                                        .getText()
                                        .toString()
                                        .trim();

                        String last =
                                ((EditText) lastName)
                                        .getText()
                                        .toString()
                                        .trim();

                        String user =
                                ((EditText) username)
                                        .getText()
                                        .toString()
                                        .trim()
                                        .toLowerCase(Locale.US);

                        String pass =
                                password
                                        .getText()
                                        .toString();

                        String confirm =
                                confirmPassword
                                        .getText()
                                        .toString();

                        String countryValue =
                                country
                                        .getText()
                                        .toString()
                                        .trim();

                        status.setText("");

                        if (first.length() < 2) {
                            status.setText(
                                    "Please enter your first name."
                            );
                            focus(firstName);
                            return;
                        }

                        if (last.length() < 2) {
                            status.setText(
                                    "Please enter your last name."
                            );
                            focus(lastName);
                            return;
                        }

                        if (!validUsername(user)) {
                            status.setText(
                                    "Username: 3–20 letters, numbers or _"
                            );
                            focus(username);
                            return;
                        }

                        if (!isStrongPassword(pass)) {
                            status.setText(
                                    "Password does not meet the security requirements."
                            );
                            focus(password);
                            return;
                        }

                        if (!pass.equals(confirm)) {
                            status.setText(
                                    "Passwords do not match."
                            );
                            focus(confirmPassword);
                            return;
                        }

                        if (countryValue.length() < 2) {
                            status.setText(
                                    "Please select your country."
                            );
                            country.performClick();
                            return;
                        }

                        if (!terms.isChecked()) {
                            status.setText(
                                    "Please accept the Terms of Use and Privacy Policy."
                            );
                            return;
                        }

                        if (accountStore.contains("username")) {

                            String oldUser =
                                    accountStore.getString(
                                            "username",
                                            ""
                                    );

                            if (oldUser.equalsIgnoreCase(user)) {

                                status.setText(
                                        "This username already exists on this device."
                                );

                                focus(username);
                                return;
                            }
                        }

                        create.setEnabled(false);
                        create.setText(
                                "CREATING YOUR ACCOUNT..."
                        );

                        hideKeyboard();

                        final String finalFirst = first;
                        final String finalLast = last;
                        final String finalUser = user;
                        final String finalCountry = countryValue;
                        final String hash = hashPassword(pass);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        accountStore.edit()
                                                .putBoolean(
                                                        "logged_in",
                                                        true
                                                )
                                                .putString(
                                                        "first_name",
                                                        finalFirst
                                                )
                                                .putString(
                                                        "last_name",
                                                        finalLast
                                                )
                                                .putString(
                                                        "username",
                                                        finalUser
                                                )
                                                .putString(
                                                        "country",
                                                        finalCountry
                                                )
                                                .putString(
                                                        "password_hash",
                                                        hash
                                                )
                                                .apply();

                                        showAccountCreated(
                                                finalFirst,
                                                finalLast,
                                                finalUser
                                        );
                                    }
                                },
                                900
                        );
                    }
                }
        );
    }

    // ============================================================
    // ACCOUNT CREATION HELPERS
    // ============================================================

    // ============================================================
    // PROFESSIONAL PASSWORD EYE ICON
    // ============================================================

    private class PasswordEyeView extends View {

        private final Paint paint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        private boolean visible = false;

        PasswordEyeView(Context context) {
            super(context);

            setContentDescription(
                    "Show or hide password"
            );

            paint.setStrokeCap(
                    Paint.Cap.ROUND
            );

            paint.setStrokeJoin(
                    Paint.Join.ROUND
            );
        }

        void setVisible(boolean value) {
            visible = value;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();

            float cx = getWidth() / 2f;
            float cy = getHeight() / 2f;

            // Compact, balanced icon with generous whitespace.
            float w = dp(10.5f);
            float h = dp(6.5f);

            Path outline = new Path();
            outline.moveTo(cx - w, cy);

            outline.cubicTo(
                    cx - dp(6.5f), cy - dp(4.0f),
                    cx - dp(3.5f), cy - h,
                    cx, cy - h
            );

            outline.cubicTo(
                    cx + dp(3.5f), cy - h,
                    cx + dp(6.5f), cy - dp(4.0f),
                    cx + w, cy
            );

            outline.cubicTo(
                    cx + dp(6.5f), cy + dp(4.0f),
                    cx + dp(3.5f), cy + h,
                    cx, cy + h
            );

            outline.cubicTo(
                    cx - dp(3.5f), cy + h,
                    cx - dp(6.5f), cy + dp(4.0f),
                    cx - w, cy
            );

            paint.setColor(Color.rgb(92, 108, 126));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(1.45f));
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);

            canvas.drawPath(outline, paint);

            // Iris and pupil.
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(92, 108, 126));

            canvas.drawCircle(
                    cx,
                    cy,
                    dp(3.0f),
                    paint
            );

            paint.setColor(Color.WHITE);

            canvas.drawCircle(
                    cx,
                    cy,
                    dp(1.15f),
                    paint
            );

            // Fine diagonal slash when the password is hidden.
            if (!visible) {
                paint.setColor(Color.rgb(92, 108, 126));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(dp(1.55f));
                paint.setStrokeCap(Paint.Cap.ROUND);

                canvas.drawLine(
                        cx - dp(10.5f),
                        cy + dp(8.5f),
                        cx + dp(10.5f),
                        cy - dp(8.5f),
                        paint
                );
            }

            canvas.restore();
        }
    }

    private EditText addPasswordField(
            LinearLayout page,
            String labelText,
            String hint
    ) {

        TextView label =
                text(
                        labelText,
                        11,
                        gray,
                        Typeface.BOLD
                );

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(-1, -2);

        labelParams.topMargin = dp(16);

        page.addView(label, labelParams);

        FrameLayout container =
                new FrameLayout(this);

        LinearLayout.LayoutParams containerParams =
                new LinearLayout.LayoutParams(-1, dp(58));

        containerParams.topMargin = dp(6);

        EditText field =
                new EditText(this);

        field.setHint(hint);
        field.setTextSize(15);
        field.setSingleLine(true);
        field.setPadding(
                dp(16),
                0,
                dp(58),
                0
        );

        field.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        field.setTransformationMethod(
                android.text.method.PasswordTransformationMethod
                        .getInstance()
        );

        field.setBackgroundColor(
                Color.argb(225, 255, 255, 255)
        );

        container.addView(
                field,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        final PasswordEyeView eye =
                new PasswordEyeView(this);

        eye.setClickable(true);
        eye.setFocusable(true);

        FrameLayout.LayoutParams eyeParams =
                new FrameLayout.LayoutParams(
                        dp(54),
                        -1,
                        Gravity.RIGHT
                );

        container.addView(eye, eyeParams);

        eye.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean hidden =
                                field.getTransformationMethod()
                                        != null;

                        int cursor =
                                field.getSelectionStart();

                        if (hidden) {

                            field.setTransformationMethod(null);
                            eye.setVisible(true);

                        } else {

                            field.setTransformationMethod(
                                    android.text.method
                                            .PasswordTransformationMethod
                                            .getInstance()
                            );

                            eye.setVisible(false);
                        }

                        if (cursor >= 0) {
                            field.setSelection(
                                    Math.min(
                                            cursor,
                                            field.length()
                                    )
                            );
                        }

                        pressAnimation(eye);
                    }
                }
        );

        page.addView(container, containerParams);

        return field;
    }

    private boolean isStrongPassword(String password) {

        if (password == null ||
                password.length() < 12) {
            return false;
        }

        if (password.indexOf(' ') >= 0 ||
                password.indexOf('\t') >= 0 ||
                password.indexOf('\n') >= 0) {
            return false;
        }

        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;

        for (int i = 0; i < password.length(); i++) {

            char c = password.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                upper = true;
            } else if (c >= 'a' && c <= 'z') {
                lower = true;
            } else if (c >= '0' && c <= '9') {
                digit = true;
            } else {
                special = true;
            }
        }

        return upper &&
                lower &&
                digit &&
                special;
    }

    private void showCountryPicker(
            final EditText countryField
    ) {

        final String[] countries = new String[] {

                "Afghanistan",
                "Albania",
                "Algeria",
                "Andorra",
                "Angola",
                "Antigua and Barbuda",
                "Argentina",
                "Armenia",
                "Australia",
                "Austria",
                "Azerbaijan",
                "Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Belize",
                "Benin",
                "Bhutan",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Botswana",
                "Brazil",
                "Brunei",
                "Bulgaria",
                "Burkina Faso",
                "Burundi",
                "Cabo Verde",
                "Cambodia",
                "Cameroon",
                "Canada",
                "Central African Republic",
                "Chad",
                "Chile",
                "China",
                "Colombia",
                "Comoros",
                "Congo",
                "Costa Rica",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czechia",
                "Democratic Republic of the Congo",
                "Denmark",
                "Djibouti",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Egypt",
                "El Salvador",
                "Equatorial Guinea",
                "Eritrea",
                "Estonia",
                "Eswatini",
                "Ethiopia",
                "Fiji",
                "Finland",
                "France",
                "Gabon",
                "Gambia",
                "Georgia",
                "Germany",
                "Ghana",
                "Greece",
                "Grenada",
                "Guatemala",
                "Guinea",
                "Guinea-Bissau",
                "Guyana",
                "Haiti",
                "Honduras",
                "Hungary",
                "Iceland",
                "India",
                "Indonesia",
                "Iran",
                "Iraq",
                "Ireland",
                "Israel",
                "Italy",
                "Ivory Coast",
                "Jamaica",
                "Japan",
                "Jordan",
                "Kazakhstan",
                "Kenya",
                "Kiribati",
                "Kuwait",
                "Kyrgyzstan",
                "Laos",
                "Latvia",
                "Lebanon",
                "Lesotho",
                "Liberia",
                "Libya",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "Madagascar",
                "Malawi",
                "Malaysia",
                "Maldives",
                "Mali",
                "Malta",
                "Marshall Islands",
                "Mauritania",
                "Mauritius",
                "Mexico",
                "Micronesia",
                "Moldova",
                "Monaco",
                "Mongolia",
                "Montenegro",
                "Morocco",
                "Mozambique",
                "Myanmar",
                "Namibia",
                "Nauru",
                "Nepal",
                "Netherlands",
                "New Zealand",
                "Nicaragua",
                "Niger",
                "Nigeria",
                "North Korea",
                "North Macedonia",
                "Norway",
                "Oman",
                "Pakistan",
                "Palau",
                "Palestine",
                "Panama",
                "Papua New Guinea",
                "Paraguay",
                "Peru",
                "Philippines",
                "Poland",
                "Portugal",
                "Qatar",
                "Romania",
                "Russia",
                "Rwanda",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Senegal",
                "Serbia",
                "Seychelles",
                "Sierra Leone",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "Solomon Islands",
                "Somalia",
                "South Africa",
                "South Korea",
                "South Sudan",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Suriname",
                "Sweden",
                "Switzerland",
                "Syria",
                "Tajikistan",
                "Tanzania",
                "Thailand",
                "Timor-Leste",
                "Togo",
                "Tonga",
                "Trinidad and Tobago",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "Tuvalu",
                "Uganda",
                "Ukraine",
                "United Arab Emirates",
                "United Kingdom",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Vatican City",
                "Venezuela",
                "Vietnam",
                "Yemen",
                "Zambia",
                "Zimbabwe"
        };

        final android.app.Dialog dialog =
                new android.app.Dialog(this);

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(20),
                dp(18),
                dp(20),
                dp(18)
        );

        TextView title =
                text(
                        "Select your country",
                        20,
                        dark,
                        Typeface.BOLD
                );

        box.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        final android.widget.SearchView search =
                new android.widget.SearchView(this);

        search.setQueryHint(
                "Search countries..."
        );

        box.addView(
                search,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                )
        );

        final android.widget.ListView list =
                new android.widget.ListView(this);

        final java.util.ArrayList<String> filtered =
                new java.util.ArrayList<String>();

        for (String country : countries) {
            filtered.add(country);
        }

        final android.widget.ArrayAdapter<String> adapter =
                new android.widget.ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        filtered
                );

        list.setAdapter(adapter);

        box.addView(
                list,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(420)
                )
        );

        list.setOnItemClickListener(
                new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        countryField.setText(
                                filtered.get(position)
                        );

                        dialog.dismiss();
                    }
                }
        );

        search.setOnQueryTextListener(
                new android.widget.SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(
                            String query
                    ) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(
                            String query
                    ) {

                        String q =
                                query == null
                                        ? ""
                                        : query.trim()
                                                .toLowerCase(
                                                        Locale.US
                                                );

                        filtered.clear();

                        for (String country : countries) {

                            if (country
                                    .toLowerCase(Locale.US)
                                    .contains(q)) {

                                filtered.add(country);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        return true;
                    }
                }
        );

        dialog.setContentView(box);

        android.view.Window window =
                dialog.getWindow();

        if (window != null) {

            window.setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(
                            Color.WHITE
                    )
            );

            window.setLayout(
                    (int) (getResources()
                            .getDisplayMetrics()
                            .widthPixels * 0.92f),
                    dp(600)
            );
        }

        dialog.show();

        if (dialog.getWindow() != null) {

            dialog.getWindow().setLayout(
                    (int) (getResources()
                            .getDisplayMetrics()
                            .widthPixels * 0.92f),
                    dp(600)
            );
        }
    }

    private void showLegalPage(
            String pageType
    ) {

        root.removeAllViews();

        background = new MapBackground(this);
        root.addView(background);

        LinearLayout outer =
                new LinearLayout(this);

        outer.setOrientation(
                LinearLayout.VERTICAL
        );

        outer.setPadding(
                dp(22),
                dp(22),
                dp(22),
                dp(22)
        );

        TextView back =
                text(
                        "‹   BACK",
                        13,
                        blue,
                        Typeface.BOLD
                );

        outer.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
                )
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressAnimation(v);
                        showCreateAccount();
                    }
                }
        );

        TextView title =
                text(
                        pageType,
                        27,
                        dark,
                        Typeface.BOLD
                );

        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        titleParams.bottomMargin = dp(15);

        outer.addView(title, titleParams);

        ScrollView scroll =
                new ScrollView(this);

        TextView content;

        if ("TERMS OF USE".equals(pageType)) {

            content = text(
                    "HORUS — TERMS OF USE\n\n" +

                    "Last updated: October 2026\n\n" +

                    "Welcome to Horus. These Terms of Use describe the basic rules for using Horus applications and services.\n\n" +

                    "1. ACCEPTANCE\n\n" +
                    "By creating or using a Horus account, you agree to these Terms of Use and to use Horus responsibly and lawfully.\n\n" +

                    "2. HORUS ACCOUNT\n\n" +
                    "You are responsible for maintaining the confidentiality of your account credentials and for activity performed through your account. You should provide accurate information when creating an account and keep it reasonably up to date.\n\n" +

                    "3. ACCEPTABLE USE\n\n" +
                    "You must not use Horus to abuse, attack, disrupt, reverse engineer, or unlawfully interfere with Horus services, infrastructure, users, or data. You must not use the service for fraudulent or illegal activity.\n\n" +

                    "4. MAP AND GEOGRAPHIC DATA\n\n" +
                    "Horus may provide geographic, mapping, routing, location, and other geospatial information. Such information may contain errors, omissions, delays, or changes. Users should exercise appropriate judgment and should not rely solely on Horus for safety-critical decisions.\n\n" +

                    "5. INTELLECTUAL PROPERTY\n\n" +
                    "Horus software, branding, interface designs, databases, and original content are protected by applicable intellectual-property laws. Nothing in these Terms grants ownership of Horus intellectual property to a user.\n\n" +

                    "6. SERVICE CHANGES\n\n" +
                    "Horus may add, modify, suspend, or discontinue features as the platform develops.\n\n" +

                    "7. ACCOUNT SUSPENSION\n\n" +
                    "Accounts may be restricted or suspended when necessary to protect the service, users, security, or legal compliance.\n\n" +

                    "8. LIMITATION OF LIABILITY\n\n" +
                    "To the extent permitted by applicable law, Horus is not responsible for losses resulting from misuse of the service, inaccurate third-party information, network failures, or reliance on information that is incomplete or outdated.\n\n" +

                    "9. UPDATES TO THESE TERMS\n\n" +
                    "These Terms may be updated as Horus evolves. Material changes will be communicated through appropriate product channels where applicable.\n\n" +

                    "10. CONTACT\n\n" +
                    "For questions about these Terms, users may contact the official Horus support or company contact channel provided by Horus.\n\n" +

                    "These Terms are a product-stage draft and may be replaced by a formally reviewed legal agreement before public commercial launch.",
                    14,
                    dark,
                    Typeface.NORMAL
            );

        } else {

            content = text(
                    "HORUS — PRIVACY POLICY\n\n" +

                    "Last updated: October 2026\n\n" +

                    "This Privacy Policy explains the types of information Horus may collect and how that information may be used as the Horus platform develops.\n\n" +

                    "1. INFORMATION YOU PROVIDE\n\n" +
                    "An account may contain information such as your first name, last name, username, country, and account credentials.\n\n" +

                    "2. ACCOUNT CREDENTIALS\n\n" +
                    "Horus should not store your account password as ordinary readable text. The current prototype stores a cryptographic password hash locally on the device. Production authentication will use a dedicated backend security architecture and an appropriate password-hashing scheme.\n\n" +

                    "3. LOCATION INFORMATION\n\n" +
                    "Horus Maps may eventually request or process location information when a user chooses features that require it, such as displaying the user's position or providing navigation. Location access should be requested only when needed for a relevant feature.\n\n" +

                    "4. HOW INFORMATION MAY BE USED\n\n" +
                    "Information may be used to create and maintain accounts, provide requested features, improve reliability, protect the service, prevent abuse, and develop Horus products.\n\n" +

                    "5. DATA SHARING\n\n" +
                    "Horus should not sell personal information as part of ordinary operation. Information may need to be processed by infrastructure or service providers when necessary to operate requested features, subject to applicable agreements and safeguards.\n\n" +

                    "6. SECURITY\n\n" +
                    "Horus will use reasonable technical and organizational measures to protect information. No internet-connected system can guarantee absolute security.\n\n" +

                    "7. DATA RETENTION\n\n" +
                    "Information should be retained only for as long as reasonably necessary for the relevant purpose, legal obligations, security, dispute resolution, or legitimate operational requirements.\n\n" +

                    "8. YOUR CONTROLS\n\n" +
                    "As Horus develops, users will receive appropriate controls for accessing, correcting, exporting, or deleting eligible account information, subject to applicable law and technical requirements.\n\n" +

                    "9. CHILDREN AND YOUNG USERS\n\n" +
                    "Age requirements and protections will be implemented according to applicable laws and the requirements of the markets where Horus operates.\n\n" +

                    "10. POLICY UPDATES\n\n" +
                    "This Privacy Policy may change as Horus adds new products, infrastructure, and data-processing capabilities. Updated versions will identify their effective date.\n\n" +

                    "11. CONTACT\n\n" +
                    "Privacy questions may be directed to the official Horus support or company contact channel provided by Horus.\n\n" +

                    "This document is a product-stage privacy-policy draft and should receive formal legal and regulatory review before a public commercial launch.",
                    14,
                    dark,
                    Typeface.NORMAL
            );
        }

        content.setPadding(
                dp(4),
                dp(5),
                dp(4),
                dp(35)
        );

        scroll.addView(content);

        outer.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        root.addView(
                outer,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        outer.setAlpha(0f);
        outer.setTranslationY(dp(25));

        outer.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(500)
                .setInterpolator(
                        new DecelerateInterpolator()
                )
                .start();
    }

    // ============================================================
    // ACCOUNT CREATED
    // ============================================================

    private void showAccountCreated(
            String first,
            String last,
            String username
    ) {

        root.removeAllViews();

        background =
                new MapBackground(this);

        root.addView(background);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setGravity(
                Gravity.CENTER
        );

        page.setPadding(
                dp(28),
                dp(25),
                dp(28),
                dp(25)
        );

        TextView check =
                text(
                        "✓",
                        60,
                        Color.rgb(35, 170, 130),
                        Typeface.BOLD
                );

        check.setGravity(
                Gravity.CENTER
        );

        FrameLayout checkCard =
                new FrameLayout(this);

        checkCard.setBackground(
                rounded(
                        Color.argb(28, 35, 170, 130),
                        50
                )
        );

        checkCard.addView(
                check,
                new FrameLayout.LayoutParams(
                        dp(100),
                        dp(100)
                )
        );

        page.addView(
                checkCard,
                new LinearLayout.LayoutParams(
                        dp(100),
                        dp(100)
                )
        );

        TextView title =
                text(
                        "Account created!",
                        30,
                        dark,
                        Typeface.BOLD
                );

        title.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        titleParams.topMargin =
                dp(25);

        page.addView(
                title,
                titleParams
        );

        TextView welcome =
                text(
                        "Welcome, " + first + " " + last,
                        17,
                        gray,
                        Typeface.NORMAL
                );

        welcome.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams welcomeParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        welcomeParams.topMargin =
                dp(9);

        page.addView(
                welcome,
                welcomeParams
        );

        TextView identity =
                text(
                        username + "@horus.com",
                        15,
                        blue,
                        Typeface.BOLD
                );

        identity.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams identityParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        identityParams.topMargin =
                dp(15);

        page.addView(
                identity,
                identityParams
        );

        TextView continueButton =
                button(
                        "ENTER HORUS  〉",
                        16
                );

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                );

        buttonParams.topMargin =
                dp(35);

        page.addView(
                continueButton,
                buttonParams
        );

        root.addView(
                page,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        page.setAlpha(0f);
        page.setScaleX(0.92f);
        page.setScaleY(0.92f);

        page.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setInterpolator(
                        new OvershootInterpolator(1.05f)
                )
                .start();

        continueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressAnimation(v);

                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showDashboard();
                                    }
                                },
                                180
                        );
                    }
                }
        );
    }

    // ============================================================
    // DASHBOARD
    // ============================================================

    private void showDashboard() {

        root.removeAllViews();

        background =
                new MapBackground(this);

        root.addView(background);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setPadding(
                dp(24),
                dp(25),
                dp(24),
                dp(24)
        );

        TextView top =
                text(
                        "HORUS MAPS",
                        21,
                        dark,
                        Typeface.BOLD
                );

        top.setLetterSpacing(0.10f);

        page.addView(
                top,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        String first =
                accountStore.getString(
                        "first_name",
                        "Explorer"
                );

        String username =
                accountStore.getString(
                        "username",
                        "user"
                );

        TextView greeting =
                text(
                        "Good to see you, " + first + " 👋",
                        29,
                        dark,
                        Typeface.BOLD
                );

        LinearLayout.LayoutParams greetingParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        greetingParams.topMargin =
                dp(25);

        page.addView(
                greeting,
                greetingParams
        );

        TextView id =
                text(
                        username + "@horus.com",
                        14,
                        blue,
                        Typeface.BOLD
                );

        LinearLayout.LayoutParams idParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        idParams.topMargin =
                dp(8);

        page.addView(
                id,
                idParams
        );

        Space space =
                new Space(this);

        page.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.25f
                )
        );

        TextView mapCard =
                text(
                        "🌍\n\nHORUS MAP\n\nYour world is waiting.",
                        20,
                        dark,
                        Typeface.BOLD
                );

        mapCard.setGravity(
                Gravity.CENTER
        );

        mapCard.setLineSpacing(
                dp(5),
                1f
        );

        mapCard.setBackground(
                rounded(
                        Color.argb(225, 255, 255, 255),
                        28
                )
        );

        page.addView(
                mapCard,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(250)
                )
        );

        Space bottom =
                new Space(this);

        page.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.25f
                )
        );

        TextView maps =
                button(
                        "EXPLORE HORUS MAPS",
                        15
                );

        page.addView(
                maps,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView profile =
                outlineButton(
                        "MY PROFILE",
                        15
                );

        LinearLayout.LayoutParams profileParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                );

        profileParams.topMargin =
                dp(12);

        page.addView(
                profile,
                profileParams
        );

        root.addView(
                page,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        page.setAlpha(0f);
        page.setTranslationY(dp(35));

        page.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(700)
                .setInterpolator(
                        new DecelerateInterpolator()
                )
                .start();
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private void showLogin() {

        root.removeAllViews();

        background = new MapBackground(this);
        root.addView(background);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(
                dp(26),
                dp(24),
                dp(26),
                dp(24)
        );

        TextView back = text(
                "‹   BACK",
                13,
                blue,
                Typeface.BOLD
        );

        back.setGravity(Gravity.CENTER_VERTICAL);

        page.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
                )
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAuthChoice();
                    }
                }
        );

        Space topSpace = new Space(this);

        page.addView(
                topSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.45f
                )
        );

        FrameLayout logo = createLogoCard(82);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        dp(82),
                        dp(82)
                );

        logoParams.gravity =
                Gravity.CENTER_HORIZONTAL;

        page.addView(
                logo,
                logoParams
        );

        TextView title = text(
                "Welcome back",
                30,
                dark,
                Typeface.BOLD
        );

        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        titleParams.topMargin = dp(20);

        page.addView(
                title,
                titleParams
        );

        TextView subtitle = text(
                "Sign in to continue to Horus Maps",
                14,
                Color.rgb(105, 115, 130),
                Typeface.NORMAL
        );

        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        subtitleParams.topMargin = dp(7);
        subtitleParams.bottomMargin = dp(26);

        page.addView(
                subtitle,
                subtitleParams
        );

        TextView username = input(
                "Username",
                false
        );

        addField(
                page,
                "HORUS USERNAME",
                username
        );

        EditText password =
                addPasswordField(
                        page,
                        "PASSWORD",
                        "Password"
                );


        TextView forgot = text(
                "Forgot password?",
                13,
                blue,
                Typeface.BOLD
        );

        forgot.setGravity(
                Gravity.RIGHT | Gravity.CENTER_VERTICAL
        );

        LinearLayout.LayoutParams forgotParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                );

        page.addView(
                forgot,
                forgotParams
        );

        TextView status = text(
                "",
                12,
                Color.rgb(210, 60, 70),
                Typeface.BOLD
        );

        status.setGravity(Gravity.CENTER);

        status.setMinHeight(dp(32));

        page.addView(
                status,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(38)
                )
        );

        TextView login = button(
                "LOGIN TO HORUS",
                16
        );

        page.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        TextView create = text(
                "Don't have a Horus account?  Create Account",
                13,
                blue,
                Typeface.BOLD
        );

        create.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        createParams.topMargin = dp(10);

        page.addView(
                create,
                createParams
        );

        Space bottomSpace = new Space(this);

        page.addView(
                bottomSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        0.35f
                )
        );

        TextView securityNote = text(
                "Your account credentials stay on this device in this prototype.",
                11,
                Color.rgb(125, 132, 145),
                Typeface.NORMAL
        );

        securityNote.setGravity(Gravity.CENTER);

        page.addView(
                securityNote,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        root.addView(
                page,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        // ========================================================
        // PROFESSIONAL LOGIN ENTRANCE ANIMATION
        // ========================================================

        background.setAlpha(0f);

        page.setAlpha(0f);
        page.setTranslationY(dp(28));

        root.post(
                new Runnable() {
                    @Override
                    public void run() {

                        background.animate()
                                .alpha(1f)
                                .setDuration(450)
                                .setInterpolator(
                                        new DecelerateInterpolator()
                                )
                                .start();

                        page.animate()
                                .alpha(1f)
                                .translationY(0f)
                                .setDuration(600)
                                .setStartDelay(70)
                                .setInterpolator(
                                        new DecelerateInterpolator()
                                )
                                .start();
                    }
                }
        );

        forgot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        status.setTextColor(
                                Color.rgb(70, 95, 125)
                        );

                        status.setText(
                                "Password recovery will be available with the Horus online account system."
                        );
                    }
                }
        );

        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCreateAccount();
                    }
                }
        );

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String user =
                                username.getText()
                                        .toString()
                                        .trim()
                                        .toLowerCase(
                                                Locale.US
                                        );

                        // Accept both:
                        // username
                        // username@horus.com
                        if (user.endsWith("@horus.com")) {
                            user = user.substring(
                                    0,
                                    user.length() -
                                    "@horus.com".length()
                            );
                        }

                        String pass =
                                password.getText()
                                        .toString();

                        status.setTextColor(
                                Color.rgb(210, 60, 70)
                        );

                        if (user.length() == 0) {

                            status.setText(
                                    "Enter your Horus username."
                            );

                            username.requestFocus();
                            return;
                        }

                        if (pass.length() == 0) {

                            status.setText(
                                    "Enter your password."
                            );

                            password.requestFocus();
                            return;
                        }

                        if (!accountStore.contains(
                                "username"
                        )) {

                            status.setText(
                                    "No Horus account exists on this device."
                            );

                            return;
                        }

                        String savedUser =
                                accountStore.getString(
                                        "username",
                                        ""
                                );

                        String savedHash =
                                accountStore.getString(
                                        "password_hash",
                                        ""
                                );

                        if (!savedUser.equals(user)
                                || !savedHash.equals(
                                        hashPassword(pass)
                                )) {

                            status.setText(
                                    "Incorrect username or password."
                            );

                            password.setText("");
                            password.requestFocus();

                            return;
                        }

                        login.setText(
                                "SIGNING IN..."
                        );

                        login.setEnabled(false);

                        status.setTextColor(
                                Color.rgb(45, 145, 95)
                        );

                        status.setText(
                                "Login successful."
                        );

                        accountStore.edit()
                                .putBoolean(
                                        "logged_in",
                                        true
                                )
                                .apply();

                        hideKeyboard();

                        // Smooth transition to Dashboard.
                        final View currentPage = root;

                        currentPage.animate()
                                .alpha(0f)
                                .setDuration(220)
                                .withEndAction(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                try {

                                                    showDashboard();

                                                    root.setAlpha(0f);

                                                    root.animate()
                                                            .alpha(1f)
                                                            .setDuration(320)
                                                            .start();

                                                } catch (Throwable e) {

                                                    root.setAlpha(1f);

                                                    status.setTextColor(
                                                            Color.rgb(
                                                                    210,
                                                                    60,
                                                                    70
                                                            )
                                                    );

                                                    status.setText(
                                                            "Dashboard error: " +
                                                            e.getClass()
                                                                    .getSimpleName()
                                                    );

                                                    login.setText(
                                                            "LOGIN TO HORUS"
                                                    );

                                                    login.setEnabled(true);

                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                )
                                .start();
                    }
                }
        );
    }

    // ============================================================
    // INPUT
    // ============================================================

    private TextView input(
            String hint,
            boolean password
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(15);
        e.setTextColor(dark);
        e.setHintTextColor(
                Color.rgb(145, 160, 175)
        );

        e.setSingleLine(true);

        e.setPadding(
                dp(17),
                0,
                dp(17),
                0
        );

        if (password) {

            e.setInputType(
                    android.text.InputType.TYPE_CLASS_TEXT |
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

        } else {

            e.setInputType(
                    android.text.InputType.TYPE_CLASS_TEXT
            );
        }

        GradientDrawable bg =
                rounded(
                        Color.WHITE,
                        16
                );

        bg.setStroke(
                dp(1),
                Color.argb(
                        45,
                        20,
                        105,
                        190
                )
        );

        e.setBackground(bg);

        e.setElevation(dp(1));

        return e;
    }

    private void addField(
            LinearLayout page,
            String label,
            TextView field
    ) {

        TextView labelView =
                text(
                        label,
                        10,
                        blue,
                        Typeface.BOLD
                );

        labelView.setLetterSpacing(
                0.12f
        );

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(25)
                );

        labelParams.topMargin =
                dp(19);

        page.addView(
                labelView,
                labelParams
        );

        page.addView(
                field,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(57)
                )
        );
    }

    // ============================================================
    // VALIDATION / SECURITY
    // ============================================================

    private boolean validUsername(
            String username
    ) {

        if (username == null) {
            return false;
        }

        if (username.length() < 3 ||
                username.length() > 20) {
            return false;
        }

        for (int i = 0;
             i < username.length();
             i++) {

            char c =
                    username.charAt(i);

            if (!(
                    (c >= 'a' && c <= 'z') ||
                    (c >= '0' && c <= '9') ||
                    c == '_'
            )) {
                return false;
            }
        }

        return true;
    }

    private String hashPassword(
            String password
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] result =
                    digest.digest(
                            password.getBytes(
                                    "UTF-8"
                            )
                    );

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : result) {

                sb.append(
                        String.format(
                                Locale.US,
                                "%02x",
                                b & 0xff
                        )
                );
            }

            return sb.toString();

        } catch (Exception e) {

            return "";
        }
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

    private TextView text(
            String value,
            float size,
            int color,
            int style
    ) {

        TextView t =
                new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        t.setTypeface(
                Typeface.create(
                        "sans",
                        style
                )
        );

        return t;
    }

    private TextView button(
            String value,
            float size
    ) {

        TextView b =
                text(
                        value,
                        size,
                        Color.WHITE,
                        Typeface.BOLD
                );

        b.setGravity(
                Gravity.CENTER
        );

        b.setLetterSpacing(
                0.05f
        );

        GradientDrawable bg =
                new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[] {
                                blue,
                                cyan
                        }
                );

        bg.setCornerRadius(
                dp(18)
        );

        b.setBackground(bg);

        b.setElevation(dp(5));

        return b;
    }

    private TextView outlineButton(
            String value,
            float size
    ) {

        TextView b =
                text(
                        value,
                        size,
                        blue,
                        Typeface.BOLD
                );

        b.setGravity(
                Gravity.CENTER
        );

        b.setLetterSpacing(
                0.05f
        );

        GradientDrawable bg =
                rounded(
                        Color.WHITE,
                        18
                );

        bg.setStroke(
                dp(2),
                Color.argb(
                        80,
                        20,
                        105,
                        190
                )
        );

        b.setBackground(bg);
        b.setElevation(dp(2));

        return b;
    }

    private GradientDrawable rounded(
            int color,
            float radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(color);
        g.setCornerRadius(dp(radius));

        return g;
    }

    private FrameLayout createLogoCard(
            int size
    ) {

        FrameLayout card =
                new FrameLayout(this);

        GradientDrawable bg =
                rounded(
                        Color.WHITE,
                        25
                );

        bg.setStroke(
                dp(1),
                Color.argb(
                        35,
                        20,
                        105,
                        190
                )
        );

        card.setBackground(bg);
        card.setElevation(dp(8));

        ImageView logo =
                new ImageView(this);

        logo.setImageResource(
                R.mipmap.ic_launcher
        );

        logo.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        logo.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(8)
        );

        card.addView(
                logo,
                new FrameLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        return card;
    }

    private void addFeature(
            LinearLayout parent,
            String top,
            String bottom
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                rounded(
                        Color.argb(
                                220,
                                255,
                                255,
                                255
                        ),
                        16
                );

        bg.setStroke(
                dp(1),
                Color.argb(
                        30,
                        20,
                        105,
                        190
                )
        );

        card.setBackground(bg);

        TextView a =
                text(
                        top,
                        10,
                        blue,
                        Typeface.BOLD
                );

        a.setGravity(
                Gravity.CENTER
        );

        a.setLetterSpacing(
                0.08f
        );

        TextView b =
                text(
                        bottom,
                        9,
                        gray,
                        Typeface.NORMAL
                );

        b.setGravity(
                Gravity.CENTER
        );

        card.addView(a);
        card.addView(b);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1f
                );

        p.leftMargin = dp(4);
        p.rightMargin = dp(4);

        parent.addView(
                card,
                p
        );
    }

    private void pressAnimation(
            final View v
    ) {

        v.animate()
                .scaleX(0.96f)
                .scaleY(0.96f)
                .setDuration(90)
                .withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {

                                v.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(120)
                                        .start();
                            }
                        }
                )
                .start();
    }

    private void focus(
            View view
    ) {

        view.requestFocus();

        if (view instanceof EditText) {

            ((EditText) view)
                    .postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {

                                    InputMethodManager imm =
                                            (InputMethodManager)
                                                    getSystemService(
                                                            INPUT_METHOD_SERVICE
                                                    );

                                    imm.showSoftInput(
                                            view,
                                            InputMethodManager.SHOW_IMPLICIT
                                    );
                                }
                            },
                            150
                    );
        }
    }

    private void hideKeyboard() {

        View current =
                getCurrentFocus();

        if (current == null) {
            return;
        }

        InputMethodManager imm =
                (InputMethodManager)
                        getSystemService(
                                INPUT_METHOD_SERVICE
                        );

        imm.hideSoftInputFromWindow(
                current.getWindowToken(),
                0
        );
    }

    private int dp(float value) {

        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    // ============================================================
    // ANIMATED MAP BACKGROUND
    // ============================================================

    private class MapBackground
            extends View {

        private Paint paint =
                new Paint(
                        Paint.ANTI_ALIAS_FLAG
                );

        private Paint linePaint =
                new Paint(
                        Paint.ANTI_ALIAS_FLAG
                );

        private float progress = 0f;

        private ValueAnimator animator;

        public MapBackground(
                android.content.Context context
        ) {

            super(context);

            paint.setStyle(
                    Paint.Style.FILL
            );

            linePaint.setStyle(
                    Paint.Style.STROKE
            );

            linePaint.setStrokeWidth(
                    dp(1)
            );

            linePaint.setColor(
                    Color.argb(
                            35,
                            20,
                            105,
                            190
                    )
            );

            animator =
                    ValueAnimator.ofFloat(
                            0f,
                            1f
                    );

            animator.setDuration(9000);

            animator.setRepeatCount(
                    ValueAnimator.INFINITE
            );

            animator.addUpdateListener(
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(
                                ValueAnimator animation
                        ) {

                            progress =
                                    (Float)
                                            animation
                                                    .getAnimatedValue();

                            invalidate();
                        }
                    }
            );

            animator.start();
        }

        @Override
        protected void onDraw(
                Canvas canvas
        ) {

            super.onDraw(canvas);

            float w = getWidth();
            float h = getHeight();

            canvas.drawColor(
                    Color.rgb(
                            248,
                            251,
                            253
                    )
            );

            RadialGradient glow =
                    new RadialGradient(
                            w *
                                    (
                                            0.20f +
                                            progress *
                                            0.20f
                                    ),
                            h * 0.18f,
                            w * 0.65f,
                            new int[] {
                                    Color.argb(
                                            55,
                                            35,
                                            190,
                                            210
                                    ),
                                    Color.argb(
                                            18,
                                            20,
                                            105,
                                            190
                                    ),
                                    Color.TRANSPARENT
                            },
                            new float[] {
                                    0f,
                                    0.45f,
                                    1f
                            },
                            Shader.TileMode.CLAMP
                    );

            paint.setShader(glow);

            canvas.drawRect(
                    0,
                    0,
                    w,
                    h,
                    paint
            );

            paint.setShader(null);

            for (int i = 0;
                 i < 9;
                 i++) {

                float y =
                        h * 0.08f +
                        i * h * 0.115f;

                Path p =
                        new Path();

                p.moveTo(
                        -30,
                        y +
                                (float)
                                        Math.sin(
                                                progress *
                                                        Math.PI *
                                                        2 +
                                                        i
                                        ) *
                                        10
                );

                for (
                        int x = 0;
                        x <= w + 50;
                        x += 45
                ) {

                    float yy =
                            y +
                                    (float)
                                            Math.sin(
                                                    x *
                                                            0.009 +
                                                            progress *
                                                            Math.PI *
                                                            2 +
                                                            i
                                            ) *
                                            14;

                    p.lineTo(
                            x,
                            yy
                    );
                }

                canvas.drawPath(
                        p,
                        linePaint
                );
            }

            for (int i = 0;
                 i < 6;
                 i++) {

                float x =
                        w * 0.08f +
                        i * w * 0.18f;

                Path p =
                        new Path();

                p.moveTo(
                        x,
                        -20
                );

                for (
                        int y = 0;
                        y <= h + 40;
                        y += 45
                ) {

                    float xx =
                            x +
                                    (float)
                                            Math.sin(
                                                    y *
                                                            0.007 +
                                                            progress *
                                                            Math.PI *
                                                            2 +
                                                            i
                                            ) *
                                            17;

                    p.lineTo(
                            xx,
                            y
                    );
                }

                canvas.drawPath(
                        p,
                        linePaint
                );
            }

            paint.setColor(
                    Color.argb(
                            75,
                            35,
                            190,
                            210
                    )
            );

            for (int i = 0;
                 i < 14;
                 i++) {

                float x =
                        (float)
                                (
                                        (
                                                i * 127 +
                                                progress *
                                                250
                                        )
                                                %
                                                (w + 100)
                                ) -
                                50;

                float y =
                        h *
                                (
                                        0.12f +
                                        (i % 7) *
                                        0.12f
                                );

                float radius =
                        dp(2) +
                                (float)
                                        Math.sin(
                                                progress *
                                                        Math.PI *
                                                        2 +
                                                        i
                                        ) *
                                        dp(1);

                canvas.drawCircle(
                        x,
                        y,
                        radius,
                        paint
                );
            }
        }

        @Override
        protected void onDetachedFromWindow() {

            if (animator != null) {
                animator.cancel();
            }

            super.onDetachedFromWindow();
        }
    }
}
