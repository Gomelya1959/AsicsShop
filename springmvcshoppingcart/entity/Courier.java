package org.springmvcshoppingcart.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "Courier")
public class Courier implements Serializable {

    private String id;
    private String name;
    private String phone;
    private String user_name;

    private List<Order> orderList;

    public Courier() {
    }

    @Id
    @Column(name = "Id", length = 50, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column(name = "Name", length = 20, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Phone", length = 20, nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURIER_ID",
            referencedColumnName = "Id")

    public List<Order> getOrderList() { return orderList; }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Column(name = "Courier_name", length = 20)

    public String getUser_name() { return user_name; }

    public void setUser_name(String user_name) { this.user_name = user_name; }
}
