package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import server.Handlers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainServer {
    public static void main(String[] args) throws IOException {
        // Create the HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Register handlers for different paths
        server.createContext("/", new HomeHandler());
        server.createContext("/keyrings", new KeyringsHandler());
        server.createContext("/keychains", new KeychainsHandler());
        server.createContext("/cart", new CartHandler());  // Added cart handler
        server.createContext("/checkout", new CheckoutHandler());

        // Add static file serving for /resources
        server.createContext("/resources", new StaticFileHandler());

        server.setExecutor(null); // Default executor for handling requests
        System.out.println("Server is running on http://localhost:8080");
        server.start();
    }

    // Handler to serve static files
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String filePath = "src" + exchange.getRequestURI().getPath();
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                exchange.sendResponseHeaders(200, fileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } catch (IOException e) {
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().close();
            }
        }
    }

    // Sample CartHandler
    static class CartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Get cart data (this could come from a session or local storage)
            String response = "<h1>Your Shopping Cart</h1><ul>";

            // This is a simple map to simulate cart items
            // You can use a more sophisticated method to store the cart on the server side (like in a session or database)
            Map<String, Integer> cartItems = new HashMap<>();
            cartItems.put("Product 1", 10);
            cartItems.put("Product 2", 15);

            // Iterate over the cart items and display them
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                response += "<li>" + entry.getKey() + " - $" + entry.getValue() + "</li>";
            }
            response += "</ul>";
            response += "<a href='/checkout'>Proceed to Checkout</a>";

            // Send response
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

