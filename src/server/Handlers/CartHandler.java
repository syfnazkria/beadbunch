package server.Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson; // Ensure the Gson library is imported
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import models.Item;

public class CartHandler implements HttpHandler {

    // List to store items in the cart
    private List<Item> items = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
        String response = viewCart();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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

    // Generate a view of the cart contents
    private String viewCart() {
        if (items.isEmpty()) {
            return "Your cart is empty!";
        }

        StringBuilder cartContents = new StringBuilder("Your cart:\n");
        for (Item item : items) {
            cartContents.append(item.getName()).append(" - $").append(item.getPrice()).append("\n");
        }
        cartContents.append("Total: $").append(getTotal());
        return cartContents.toString();
    }

    // Calculate the total price of all items in the cart
    private double getTotal() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getPrice();
        }
        return total;
    }
}
