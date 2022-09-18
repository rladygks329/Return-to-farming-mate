package com.android.become_a_farmer.retrofit;

import com.android.become_a_farmer.DataClass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/get-keywords")
    Call<DataClass> getKeywords();

    @FormUrlEncoded
    @POST("/get-regions-from-keyword")
    Call<DataClass> getRegions(@Field("selectedKeywords") String selectedKeywords);
}
