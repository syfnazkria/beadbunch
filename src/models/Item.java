package models;

public class Item {
    private String name;
    private double price;
    private int quantity;

    // Constructor
    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {  // Add the getQuantity() method
        return quantity;
    }

    public void setQuantity(int quantity) {  // Add a setter if needed
        this.quantity = quantity;
    }
}