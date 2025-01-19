package server.Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.CartResponse;
import models.Item;

public class CartHandler implements HttpHandler {
    private List<Item> items = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            handleGetRequest(exchange);
        } else if (method.equalsIgnoreCase("POST")) {
            handlePostRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String cartPagePath = "src/templates/cart.html"; // Path to the cart.html file
        File cartPageFile = new File(cartPagePath);

        if (cartPageFile.exists()) {
            // Read the cart.html file
            String htmlContent = Files.lines(cartPageFile.toPath()).collect(Collectors.joining("\n"));

            // Replace the placeholder with cart data
            String cartData = viewCart(); // Generate the cart table or message
            htmlContent = htmlContent.replace("{{CART_CONTENT}}", cartData);

            // Send the response
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, htmlContent.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(htmlContent.getBytes());
            }
        } else {
            // Handle missing cart.html file
            String errorMessage = "<h1>Error 404: Cart Page Not Found</h1>";
            exchange.sendResponseHeaders(404, errorMessage.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorMessage.getBytes());
            }
        }
    }



    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestData.append(line);
        }
        System.out.println("Received POST data: " + requestData.toString());  // Debugging log

        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(requestData.toString(), Item.class);
            items.add(item); // Add the item to the cart
            System.out.println("Item added to cart: " + item.getName()); // Debugging log
            String response = "Item added: " + item.getName() + " | Total: RM" + getTotal();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            System.err.println("Error parsing item data: " + e.getMessage());
        }
    }

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

    private String viewCart() {
        // Log the size of the cart to see if it's actually empty or not
        System.out.println("Cart size: " + items.size());  // Debug log to check cart size

        if (items.isEmpty()) {
            return "<p>Your cart is empty!</p>";
        }

        StringBuilder cartContents = new StringBuilder("<table><thead><tr><th>Product</th><th>Price</th><th>Quantity</th><th>Action</th></tr></thead><tbody>");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            cartContents.append("<tr><td>").append(item.getName())
                    .append("</td><td>RM").append(item.getPrice())
                    .append("</td><td>").append(item.getQuantity())
                    .append("</td><td><form action=\"/cart/remove\" method=\"POST\"><button type=\"submit\" name=\"index\" value=\"").append(i).append("\">Remove</button></form></td></tr>");
        }
        cartContents.append("</tbody></table>");
        cartContents.append("<p><strong>Total:</strong> RM").append(getTotal()).append("</p>");

        return cartContents.toString();
    }


    private double getTotal() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}