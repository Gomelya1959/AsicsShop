package org.springmvcshoppingcart.model;

public class DiscountInfo {

    private int id;
    private int quantity;
    private int value;
    private String product_code;

    public DiscountInfo() {

    }

    public DiscountInfo(int id, int quantity, int value, String product_code) {
        this.id = id;
        this.quantity = quantity;
        this.value = value;
        this.product_code = product_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getProduct_code() { return product_code; }

    public void setProduct_code(String product_code) { this.product_code = product_code; }
}
