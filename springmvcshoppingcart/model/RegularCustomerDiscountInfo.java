package org.springmvcshoppingcart.model;

public class RegularCustomerDiscountInfo {

    private int id;
    private int total;
    private int value;

    public RegularCustomerDiscountInfo(int id, int total, int value) {
        this.id = id;
        this.total = total;
        this.value = value;
    }

    public RegularCustomerDiscountInfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
