package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import server.Handlers.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class MainServer {
    public static void main(String[] args) throws IOException {
        // Create the HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Register handlers for different paths
        server.createContext("/", new HomeHandler());
        server.createContext("/keyrings", new KeyringsHandler());
        server.createContext("/keychains", new KeychainsHandler());
        server.createContext("/cart", new CartHandler());
        server.createContext("/checkout", new CheckoutHandler());
        server.createContext("/admin", new AdminPageHandler()); // Added Admin Page Handler

        // Static file serving for /resources
        server.createContext("/resources", new StaticFileHandler());

        System.out.println("Server is running on http://localhost:8080");
        server.start();
    }

    // Handler to serve the Admin Page
    static class AdminPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String adminPagePath = "src/templates/admin.html"; // Path to the admin page
            System.out.println("Looking for admin page at: " + adminPagePath);

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                File adminPage = new File(adminPagePath);

                if (adminPage.exists()) {
                    String htmlContent = Files.lines(adminPage.toPath()).collect(Collectors.joining("\n"));
                    htmlContent = htmlContent.replace("{{PRODUCT_DATA}}", readCSV("src/resources/product.csv"));
                    htmlContent = htmlContent.replace("{{ORDER_DATA}}", readCSV("src/resources/order.csv"));

                    exchange.getResponseHeaders().add("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, htmlContent.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlContent.getBytes());
                    os.close();
                } else {
                    System.err.println("Admin page not found at: " + adminPagePath);
                    String errorMessage = "Admin page not found.";
                    exchange.sendResponseHeaders(404, errorMessage.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorMessage.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        // Method to read CSV and format it into an HTML table
        private String readCSV(String filePath) {
            StringBuilder tableContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {
                    String[] columns = line.split(",");
                    tableContent.append("<tr>");
                    for (String column : columns) {
                        if (isHeader) {
                            tableContent.append("<th>").append(column.trim()).append("</th>");
                        } else {
                            tableContent.append("<td>").append(column.trim()).append("</td>");
                        }
                    }
                    tableContent.append("</tr>");
                    isHeader = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tableContent.toString();
        }
    }

    // Handler to serve static files
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String filePath = "src" + exchange.getRequestURI().getPath();
            System.out.println("Serving static file: " + filePath);

            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                exchange.sendResponseHeaders(200, fileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } catch (IOException e) {
                System.err.println("File not found: " + filePath);
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().close();
            }
        }
    }
}
