window.onload = function() {
  const ui = SwaggerUIBundle({
    url: "swagger.json", // Путь к вашему swagger.json
    dom_id: '#swagger-ui',
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    layout: "StandaloneLayout"
  });
  window.ui = ui;
};
