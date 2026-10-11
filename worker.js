export default {
  async fetch(request) {
    return new Response(
      JSON.stringify({
        app: "Horus",
        service: "Horus Maps API",
        status: "online"
      }),
      {
        headers: {
          "Content-Type": "application/json; charset=utf-8"
        }
      }
    );
  }
};
