package org.springmvcshoppingcart.model;

import java.util.Date;

public class ReservationInfo {

    private String id;
    private Date createDate;
    private String userName;
    private String productCode;
    private int numbers;
    private String email;

    public ReservationInfo() {

    }

    public ReservationInfo(String id, Date createDate, String userName, String productCode, int numbers) {
        this.id = id;
        this.createDate = createDate;
        this.userName = userName;
        this.productCode = productCode;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getProductCode() { return productCode; }

    public void setProductCode(String productCode) { this.productCode = productCode; }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
