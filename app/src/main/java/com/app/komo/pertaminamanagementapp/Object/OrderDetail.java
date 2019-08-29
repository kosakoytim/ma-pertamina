package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("3")
    int tigakg;
    @SerializedName("5")
    int limakg;
    @SerializedName("12")
    int duabelaskg;

    public OrderDetail(int tigakg, int limakg, int duabelaskg) {
        this.tigakg = tigakg;
        this.limakg = limakg;
        this.duabelaskg = duabelaskg;
    }

    public int getTigakg() {
        return tigakg;
    }

    public void setTigakg(int tigakg) {
        this.tigakg = tigakg;
    }

    public int getLimakg() {
        return limakg;
    }

    public void setLimakg(int limakg) {
        this.limakg = limakg;
    }

    public int getDuabelaskg() {
        return duabelaskg;
    }

    public void setDuabelaskg(int duabelaskg) {
        this.duabelaskg = duabelaskg;
    }
}
