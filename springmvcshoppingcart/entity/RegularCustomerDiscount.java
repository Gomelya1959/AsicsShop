package org.springmvcshoppingcart.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "RegularCustomerDiscount")
public class RegularCustomerDiscount implements Serializable {

    private int id;
    private int total;
    private int value;


    public RegularCustomerDiscount() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column (name = "Total", nullable = false)

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Column(name = "Value_discount", nullable = false)

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
