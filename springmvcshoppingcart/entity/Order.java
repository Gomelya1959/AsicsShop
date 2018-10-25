package org.springmvcshoppingcart.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Orders", //
        uniqueConstraints = { @UniqueConstraint(columnNames = "Order_Num") })
public class Order implements Serializable {

    private static final long serialVersionUID = -2576670215015463100L;

    private String id;
    private Date orderDate;
    private int orderNum;
    private double amount;
    private  double saving;
    private Delivery orderDelivery;

    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhone;
    private String delivery;
    private String courier;
    private boolean status;
    private boolean back;
    private boolean partialReturn;
    private Account account;

    @Id
    @Column(name = "ID", length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "Order_Date", nullable = false)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "Order_Num", nullable = false)
    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Column(name = "Amount", nullable = false)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "Saving", nullable = false)
    public double getSaving() {
        return saving;
    }

    public void setSaving(double saving) {
        this.saving = saving;
    }

    @Column(name = "Customer_Name", length = 255, nullable = false)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "Customer_Address", length = 255, nullable = false)
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Column(name = "Customer_Email", length = 128, nullable = false)
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Column(name = "Customer_Phone", length = 128, nullable = false)
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @Column(name = "Delivery", length = 20, nullable = false)
    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_ID",
            foreignKey = @ForeignKey(name = "ORDER_DELIVERY_FK") )

    public Delivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(Delivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    @Column(name = "COURIER_ID", length = 50)
    public String getCourier() { return courier; }

    public void setCourier(String courier) { this.courier = courier; }

    @Column(name = "Status")

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column (name = "Back")

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    @Column (name = "PartialReturn")

    public boolean isPartialReturn() {
        return partialReturn;
    }

    public void setPartialReturn(boolean partialReturn) {
        this.partialReturn = partialReturn;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Name_User", nullable = false,//
            foreignKey = @ForeignKey(name = "ORDER_USER_FK") )

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }
}
