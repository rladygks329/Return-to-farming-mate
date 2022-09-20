package com.android.become_a_farmer.retrofit;

import com.google.gson.annotations.SerializedName;

public class DataClass {

    @SerializedName("body")
    public String body;

    public DataClass(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return body;
    }
}
