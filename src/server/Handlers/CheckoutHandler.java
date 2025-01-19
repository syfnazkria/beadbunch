package server.Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CheckoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            // Serve the checkout page
            serveCheckoutPage(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            // Handle form submission and save the order to a file
            handleCheckoutSubmission(exchange);
        }
    }

    // Serve the checkout HTML form
    private void serveCheckoutPage(HttpExchange exchange) throws IOException {
        String response = new String(Files.readAllBytes(Paths.get("src/templates/checkout.html")));
        exchange.sendResponseHeaders(200, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    // Handle the checkout form submission
    private void handleCheckoutSubmission(HttpExchange exchange) throws IOException {
        // Parse the form data from the POST request
        Map<String, String> formData = parseFormData(exchange);

        // Retrieve the order details from the form
        String name = formData.get("name");
        String address = formData.get("address");
        String paymentMethod = formData.get("payment");

        // Sample product details (you can replace these with real cart data or dynamic data)
        String productName = "Teddy Bloom Keychain";
        double price = 7.99;
        int quantity = 1;

        // Log the order to the console (you can replace this with database or file logic)
        System.out.println("Order received:");
        System.out.println("Product: " + productName);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Name: " + name);
        System.out.println("Shipping Address: " + address);
        System.out.println("Payment Method: " + paymentMethod);

        // Prepare the order details in CSV format
        String orderDetails = String.format("%s,%.2f,%d,%s,%s,%s\n",
                productName, price, quantity, name, address, paymentMethod);

        // Save the order details into the 'order.txt' file (append mode)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt", true))) {
            // If the file is empty, write the header first
            if (new File("order.txt").length() == 0) {
                writer.write("Product,Price,Quantity,Name,Shipping Address,Payment Method\n");
            }
            // Append the order details to the file
            writer.write(orderDetails);
        }

        // Send a response back to the user confirming the order
        String response = "<html><body><h2>Order placed successfully!</h2><p>Thank you for your order. You will receive a confirmation email shortly.</p></body></html>";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    // Helper method to parse form data from the POST request
    private Map<String, String> parseFormData(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // Decode form data from the POST body (x-www-form-urlencoded)
        String formData = sb.toString();
        Map<String, String> formFields = new HashMap<>();
        for (String pair : formData.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                formFields.put(keyValue[0], java.net.URLDecoder.decode(keyValue[1], "UTF-8"));
            }
        }
        return formFields;
    }
}
