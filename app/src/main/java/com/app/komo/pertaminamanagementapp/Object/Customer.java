package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("name")
    String name;

    @SerializedName("address")
    String address;

    @SerializedName("nik")
    String nik;

    @SerializedName("pangkalan")
    String pangkalan;

    @SerializedName("phone")
    String phone;

    public Customer(String name, String address, String nik, String pangkalan, String phone) {
        this.name = name;
        this.address = address;
        this.nik = nik;
        this.pangkalan = pangkalan;
        this.phone = phone;
    }

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

    public String getPangkalan() {
        return pangkalan;
    }

    public void setPangkalan(String pangkalan) {
        this.pangkalan = pangkalan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
