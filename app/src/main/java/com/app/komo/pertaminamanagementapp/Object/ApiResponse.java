package com.app.komo.pertaminamanagementapp.Object;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("results")
    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
