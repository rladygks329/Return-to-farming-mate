package com.android.become_a_farmer.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @GET("/get-keywords")
    Call<DataClass> getKeywords();

    @POST("/get-regions-from-keyword")
    Call<DataClass> getRegions(@Body DataClass selectedKeywords);

    @POST("/get-regions-from-user")
    Call<DataClass> sendRating(@Body DataClass ratingDataClass);
}
