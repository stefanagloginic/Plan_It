package com.example.user01.planit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieDatabaseAPI {

    @GET("movie/now_playing/?api_key=9d0708580e01bc98415cd200cfeae74e")
    Call<MovieDatabaseModel> getMovieDatabaseModel();

}
