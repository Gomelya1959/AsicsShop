package org.springmvcshoppingcart.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Reservation")
public class Reservation implements Serializable {

    private static final long serialVersionUID = -2576670215015463100L;

    private String id;
    private Date createDate;
    private Account account;
    private Product product;
    private int numbers;
    private String email;
    private String notify;


    @Id
    @Column(name = "ID", nullable = false,length = 50)
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Date", nullable = false)

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false,
            foreignKey = @ForeignKey(name = "RESERVATION_USER_FK") )

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductCode", nullable = false,
            foreignKey = @ForeignKey(name = "RESERVATION_PRODUCT_FK") )

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    @Column(name = "Number_reservation", nullable = false)

    public int getNumbers() { return numbers; }

    public void setNumbers(int numbers) { this.numbers = numbers; }

    @Column(name = "Email", length = 20)

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Column(name = "notify", length = 20)

    public String getNotify() { return notify; }

    public void setNotify(String notify) { this.notify = notify; }
}

