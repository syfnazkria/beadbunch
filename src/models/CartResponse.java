package models;

import java.util.List;

public class CartResponse {
    private List<Item> items;
    private double total;

    public CartResponse(List<Item> items, double total) {
        this.items = items;
        this.total = total;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}
