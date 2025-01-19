package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        // Check if the product is already in the cart
        for (Product p : products) {
            if (p.getName().equals(product.getName())) {
                // Increment quantity if it exists
                p.setQuantity(p.getQuantity() + product.getQuantity());
                saveCartToCSV();
                return;
            }
        }

        // If the product is not in the cart, add it
        products.add(product);
        saveCartToCSV();
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    // Save cart data to cart.csv
    private void saveCartToCSV() {
        String filePath = "src/resources/cart.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Product,Price,Quantity,Total\n");

            for (Product product : products) {
                String line = String.format(
                        "%s,%.2f,%d,%.2f\n",
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getPrice() * product.getQuantity()
                );
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
