package com.xiangjia.locallife.network;

import com.xiangjia.locallife.network.model.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    
    @POST("workflows/run")
    Call<DifyResponse> callDifyWorkflow(
        @Header("Authorization") String token,
        @Body DifyRequest request
    );
    
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
        @Query("lat") double latitude,
        @Query("lon") double longitude,
        @Query("appid") String apiKey,
        @Query("units") String units,
        @Query("lang") String language
    );
    
    @GET("news/local")
    Call<NewsResponse> getLocalNews(
        @Query("city") String city,
        @Query("limit") int limit
    );
    
    @GET("news/detail/{id}")
    Call<NewsDetailResponse> getNewsDetail(@Path("id") String newsId);
}