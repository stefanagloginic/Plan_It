package com.example.user01.planit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventfulAPI {

    @GET("json/events/search?app_key=8nM4dcwb9jsnrp75&keywords=books")
    Call<EventfulModel> EventfulList();
}
