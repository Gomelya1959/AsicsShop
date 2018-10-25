package org.springmvcshoppingcart.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Delivery")

public class Delivery implements Serializable {

    private static final long serialVersionUID = 8550745928843183535L;

    private String id;
    private String date;
    private String time;

    @Id
    @Column (name = "ID", length = 50, nullable = false)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "DATE_DELIVERY", length = 20, nullable = false)

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Column(name = "TIME_DELIVERY", length = 20, nullable = false)

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

