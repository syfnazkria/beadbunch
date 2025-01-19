package server.Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson; // Ensure the Gson library is imported
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import models.CartResponse;
import models.Item;

public class CartHandler implements HttpHandler {
    // List to store items in the cart
    private List<Item> items = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the HTML template from the cart.html file
        String response = new String(Files.readAllBytes(Paths.get("src/templates/cart.html")));

        // Determine the HTTP method
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            handleGetRequest(exchange);
        } else if (method.equalsIgnoreCase("POST")) {
            handlePostRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }

    // Handle GET requests (view cart)
    private void handleGetRequest(HttpExchange exchange) throws IOException {
        // Read the cart.html file contents
        String response = new String(Files.readAllBytes(Paths.get("src/templates/cart.html")));
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }


    // Handle POST requests (add items to cart)
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestData = new StringBuilder();
        String line;
        // Read the request body
        while ((line = br.readLine()) != null) {
            requestData.append(line);
        }
        // Deserialize JSON data into an Item object
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(requestData.toString(), Item.class);
            items.add(item); // Add the item to the cart
            // Log the added item
            System.out.println("Item added to cart: " + item.getName() + " | Total Items: " + items.size());
            // Respond to the client
            String response = "Item added: " + item.getName() + " | Total: $" + getTotal();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // Handle invalid input
            exchange.sendResponseHeaders(400, -1); // Bad Request
            System.err.println("Error parsing item data: " + e.getMessage());
        }
    }

    // Generate a full HTML page with cart data embedded into it
    private String generateCartPage() {
        String cartData = viewCart();  // Get the cart data as a string (HTML table or empty message)

        // Return the complete HTML page with header, footer, and cart data
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Your Cart</title>" +
                "<link rel=\"stylesheet\" href=\"/resources/styles.css\">" +
                "</head>" +
                "<body>" +
                "<header>" +
                "  <div class=\"logo\"><img src=\"/resources/logo.jpg\" alt=\"BeadBunch\" /></div>" +
                "  <nav>" +
                "    <a href=\"/\">Home</a>" +
                "    <a href=\"/keyrings\">Browse Keyrings</a>" +
                "    <a href=\"/keychains\">Browse Keychains</a>" +
                "    <a href=\"/cart\">View Cart</a>" +
                "    <a href=\"/admin\">Admin Dashboard</a>" +
                "  </nav>" +
                "</header>" +
                "<h1>Your Shopping Cart</h1>" +
                "<div id=\"cartContents\">" +
                cartData +  // Insert the cart data here
                "</div>" +
                "<footer>" +
                "<p>&copy; 2025 BeadBunch. All Rights Reserved. <a href=\"/\">Home</a></p>" +
                "</footer>" +
                "</body>" +
                "</html>";
    }

    // Generate a view of the cart contents in HTML format
    private String viewCart() {
        if (items.isEmpty()) {
            return "<p>Your cart is empty!</p>";
        }

        StringBuilder cartContents = new StringBuilder("<table><thead><tr><th>Product</th><th>Price</th><th>Quantity</th></tr></thead><tbody>");
        for (Item item : items) {
            cartContents.append("<tr><td>").append(item.getName()).append("</td><td>$").append(item.getPrice()).append("</td><td>").append(item.getQuantity()).append("</td></tr>");
        }
        cartContents.append("</tbody></table>");
        cartContents.append("<p><strong>Total:</strong> $").append(getTotal()).append("</p>");
        return cartContents.toString();
    }

    // Calculate the total price of all items in the cart
    private double getTotal() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();  // Assuming quantity is a field in the Item class
        }
        return total;
    }
}
