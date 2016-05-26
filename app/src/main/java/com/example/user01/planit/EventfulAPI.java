package com.example.user01.planit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventfulAPI {

    @GET("json/events/search?app_key=8nM4dcwb9jsnrp75")
    Call<EventfulModel> EventfulList(@Query("keywords") String keyword,
                                     @Query("location") String location);
}
