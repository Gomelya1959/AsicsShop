package org.springmvcshoppingcart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class Account implements Serializable {

    private static final long serialVersionUID = -2054386655979281969L;


    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String ROLE_COURIER = "COURIER";

    private String userName;
    private String password;
    private boolean active;
    private String userRole;
    private Double total;
    private Double discount;

    @Id
    @Column(name = "User_Name", length = 20, nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "Password", length = 20, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "Active", length = 1, nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "User_Role", length = 20, nullable = false)
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Column(name = "Total")
    public Double getTotal() { return total; }

    public void setTotal(Double total) { this.total = total; }

    @Column(name = "Discount")
    public Double getDiscount() { return discount; }

    public void setDiscount(Double discount) { this.discount = discount; }

    @Override
    public String toString()  {
        return "["+ this.userName+","+ this.password+","+ this.userRole+"]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Account &&userName.equals(((Account)obj).userName);
    }
}
