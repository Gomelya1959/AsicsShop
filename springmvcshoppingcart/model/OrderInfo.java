package org.springmvcshoppingcart.model;

import java.util.Date;
import java.util.List;

public class OrderInfo {

    private String id;
    private Date orderDate;
    private int orderNum;
    private double amount;
    private double saving;

    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhone;
    private String delivery;

    private List<OrderDetailInfo> details;

    private String deliveryInfoId;
    private String deliveryInfoDate;
    private String deliveryInfoTime;
    private String courierName;
    private String courierPhone;
    private boolean status;
    private boolean back;
    private boolean partialReturn;
    private String user_name;

    public OrderInfo() {

    }

    // Using for Hibernate Query.
    // Sử dụng cho Hibernate Query.
    public OrderInfo(String id, Date orderDate, int orderNum, //
                     double amount, double saving, String customerName, String customerAddress, //
                     String customerEmail, String customerPhone, String delivery, String deliveryInfoId,
                     String deliveryInfoDate, String deliveryInfoTime) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.amount = amount;
        this.saving = saving;

        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.delivery = delivery;
        this.deliveryInfoId = deliveryInfoId;
        this.deliveryInfoDate = deliveryInfoDate;
        this.deliveryInfoTime = deliveryInfoTime;
    }

    public OrderInfo(String id, Date orderDate, int orderNum, //
                     double amount, double saving, String customerName, String customerAddress, //
                     String customerEmail, String customerPhone, String delivery, String deliveryInfoId,
                     String deliveryInfoDate, String deliveryInfoTime, boolean status, boolean back, boolean partialReturn) {
        this(id, orderDate, orderNum, amount,saving, customerName, customerAddress, customerEmail, customerPhone, delivery,
                deliveryInfoId, deliveryInfoDate, deliveryInfoTime);
        this.status = status;
        this.back = back;
        this.partialReturn = partialReturn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<OrderDetailInfo> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailInfo> details) {
        this.details = details;
    }

    public double getSaving() {
        return saving;
    }

    public void setSaving(double saving) {
        this.saving = saving;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryInfoId() {
        return deliveryInfoId;
    }

    public void setDeliveryInfoId(String deliveryInfoId) {
        this.deliveryInfoId = deliveryInfoId;
    }

    public String getDeliveryInfoDate() {
        return deliveryInfoDate;
    }

    public void setDeliveryInfoDate(String deliveryInfoDate) {
        this.deliveryInfoDate = deliveryInfoDate;
    }

    public String getDeliveryInfoTime() {
        return deliveryInfoTime;
    }

    public void setDeliveryInfoTime(String deliveryInfoTime) {
        this.deliveryInfoTime = deliveryInfoTime;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public boolean isPartialReturn() {
        return partialReturn;
    }

    public void setPartialReturn(boolean partialReturn) {
        this.partialReturn = partialReturn;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
