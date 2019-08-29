package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 22/04/18.
 */

public class LoginResponse {

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("username")
    String username;

    @SerializedName("name")
    String name;

    @SerializedName("token")
    String token;


}
