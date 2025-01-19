package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeychainsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read the contents of the HTML file
        String response = new String(Files.readAllBytes(Paths.get("src/templates/keychains.html")));

        // Set the Content-Type header to "text/html"
        exchange.getResponseHeaders().set("Content-Type", "text/html");

        // Send the response headers and the body
        exchange.sendResponseHeaders(200, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());

        // Close the exchange
        exchange.close();
    }
}
