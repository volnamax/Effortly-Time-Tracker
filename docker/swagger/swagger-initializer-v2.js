// swagger-initializer-v2.js
window.onload = function() {
    const ui = SwaggerUIBundle({
        url: "swagger-v2.json", // Путь к swagger-v2.json
        dom_id: '#swagger-ui',
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        layout: "StandaloneLayout"
    });
    window.ui = ui;
};
