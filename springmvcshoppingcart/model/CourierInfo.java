package org.springmvcshoppingcart.model;

import org.springmvcshoppingcart.entity.Order;

import java.util.*;

public class CourierInfo {

    private String id;
    private String name;
    private String phone;
    private String user_name;

    private List<Order> orders;
    private boolean newCourier;

    public CourierInfo() {
    }

    public CourierInfo(String id, String name, String phone, String user_name) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isNewCourier() {
        return newCourier;
    }

    public void setNewCourier(boolean newCourier) {
        this.newCourier = newCourier;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
