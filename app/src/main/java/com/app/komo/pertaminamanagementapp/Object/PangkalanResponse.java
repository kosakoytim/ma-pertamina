package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 22/04/18.
 */

public class PangkalanResponse {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SerializedName("username")
    String username;

    @SerializedName("name")
    String name;

    @SerializedName("owner")
    String owner;

    @SerializedName("phone")
    String phone;

}
