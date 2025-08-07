package com.xiangjia.locallife.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 新闻服务接口
 * 用于获取新闻数据
 */
public interface NewsService {
    
    /**
     * 获取最新新闻
     */
    @GET("news/latest")
    Call<String> getLatestNews(
        @Query("category") String category,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
    
    /**
     * 获取热门新闻
     */
    @GET("news/hot")
    Call<String> getHotNews(
        @Query("category") String category,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
    
    /**
     * 获取本地新闻
     */
    @GET("news/local")
    Call<String> getLocalNews(
        @Query("location") String location,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
    
    /**
     * 搜索新闻
     */
    @GET("news/search")
    Call<String> searchNews(
        @Query("keyword") String keyword,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
    
    /**
     * 获取新闻详情
     */
    @GET("news/detail")
    Call<String> getNewsDetail(
        @Query("id") String newsId,
        @Query("key") String apiKey
    );
    
    /**
     * 获取新闻分类
     */
    @GET("news/categories")
    Call<String> getNewsCategories(
        @Query("key") String apiKey
    );
    
    /**
     * 获取推荐新闻
     */
    @GET("news/recommend")
    Call<String> getRecommendNews(
        @Query("user_id") String userId,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
    
    /**
     * 获取新闻评论
     */
    @GET("news/comments")
    Call<String> getNewsComments(
        @Query("news_id") String newsId,
        @Query("page") int page,
        @Query("size") int size,
        @Query("key") String apiKey
    );
}

