package server.Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read the updated index.html template from the file system
        String response = new String(Files.readAllBytes(Paths.get("src/templates/index.html")));
        exchange.sendResponseHeaders(200, response.getBytes().length); // Send response with content length
        exchange.getResponseBody().write(response.getBytes()); // Write content to response body
        exchange.close(); // Close the exchange
    }
}
