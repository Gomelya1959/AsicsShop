package org.springmvcshoppingcart.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "Discount")
public class Discount implements Serializable {

    private int id;
    private int quantity;
    private int value;
    private Product product;

    public Discount() {

    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column (name = "Quantity", nullable = false)

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "Value_discount", nullable = false)

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false,//
            foreignKey = @ForeignKey(name = "DISCOUNT_PRODUCT_FK") )

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }
}
