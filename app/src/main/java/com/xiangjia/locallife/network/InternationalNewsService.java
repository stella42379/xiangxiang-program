package com.xiangjia.locallife.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * 国际新闻API服务接口
 */
public interface InternationalNewsService {
    
    // ============= NewsAPI 接口 =============
    @GET("v2/top-headlines")
    Call<NewsApiResponse> getTopHeadlines(
        @Query("country") String country,
        @Query("category") String category,
        @Query("sources") String sources,
        @Query("pageSize") int pageSize,
        @Query("page") int page,
        @Header("X-API-Key") String apiKey
    );
    
    @GET("v2/everything")
    Call<NewsApiResponse> searchEverything(
        @Query("q") String query,
        @Query("sources") String sources,
        @Query("domains") String domains,
        @Query("language") String language,
        @Query("sortBy") String sortBy,
        @Query("pageSize") int pageSize,
        @Query("page") int page,
        @Header("X-API-Key") String apiKey
    );

    @GET("v2/sources")
    Call<NewsSourcesResponse> getSources(
        @Query("country") String country,
        @Query("language") String language,
        @Query("category") String category,
        @Header("X-API-Key") String apiKey
    );

    // ============= Guardian API 接口 =============
    @GET("search")
    Call<GuardianResponse> getGuardianNews(
        @Query("q") String query,
        @Query("section") String section,
        @Query("order-by") String orderBy,
        @Query("show-fields") String showFields,
        @Query("page-size") int pageSize,
        @Query("page") int page,
        @Query("api-key") String apiKey
    );

    @GET("sections")
    Call<GuardianSectionsResponse> getGuardianSections(
        @Query("api-key") String apiKey
    );

    // ============= GNews API 接口 =============
    @GET("top-headlines")
    Call<GNewsResponse> getGNewsTopHeadlines(
        @Query("country") String country,
        @Query("category") String category,
        @Query("lang") String language,
        @Query("max") int max,
        @Query("token") String token
    );

    @GET("search")
    Call<GNewsResponse> searchGNews(
        @Query("q") String query,
        @Query("country") String country,
        @Query("lang") String language,
        @Query("sortby") String sortBy,
        @Query("max") int max,
        @Query("token") String token
    );
}