package com.app.komo.pertaminamanagementapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 22/04/18.
 */

public class CustomerResponse {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name")
    String name;

    @SerializedName("address")
    String address;

    @SerializedName("nik")
    String nik;

    @SerializedName("phone")
    String phone;

    @SerializedName("id")
    String id;
}
