package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Order {

    @SerializedName("customer")
    Customer customer;

    @SerializedName("pangkalan")
    String pangkalan;

    @SerializedName("timestamp")
    Long timestamp;

    @SerializedName("total")
    int total;

    @SerializedName("detail")
    OrderDetail detail;

    public Order(Customer customer, String pangkalan, Long timestamp, int total, OrderDetail detail) {
        this.customer = customer;
        this.pangkalan = pangkalan;
        this.timestamp = timestamp;
        this.total = total;
        this.detail = detail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPangkalan() {
        return pangkalan;
    }

    public void setPangkalan(String pangkalan) {
        this.pangkalan = pangkalan;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public OrderDetail getDetail() {
        return detail;
    }

    public void setDetail(OrderDetail detail) {
        this.detail = detail;
    }
}
