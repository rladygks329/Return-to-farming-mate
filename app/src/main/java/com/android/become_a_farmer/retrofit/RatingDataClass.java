package com.android.become_a_farmer.retrofit;

import com.google.gson.annotations.SerializedName;

public class RatingDataClass {
    @SerializedName("email")
    public String email;

    @SerializedName("body")
    public String body;

    public RatingDataClass(String email, String body) {
        this.email = email;
        this.body = body;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }
}
