package org.springmvcshoppingcart.model;

public class DeliveryInfo {

    private String id;

    private String date;
    private String time;

    public DeliveryInfo(String id, String date, String time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public DeliveryInfo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        return getId().equals(((DeliveryInfo)obj).getId());
    }
}
