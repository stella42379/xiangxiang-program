package com.xiangjia.locallife.network;

import com.xiangjia.locallife.model.WeatherInfo;
import com.xiangjia.locallife.network.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 其余代码保持不变

public class WeatherService {
    private static final String WEATHER_API_KEY = "your-weather-api-key";
    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    
    private ApiService apiService;
    
    public WeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        apiService = retrofit.create(ApiService.class);
    }
    
    public void getCurrentWeather(double lat, double lng, APICallback<WeatherInfo> callback) {
        Call<WeatherResponse> call = apiService.getCurrentWeather(
            lat, lng, WEATHER_API_KEY, "metric", "zh_cn"
        );
        
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherInfo weatherInfo = convertToWeatherInfo(response.body());
                    callback.onSuccess(weatherInfo);
                } else {
                    callback.onError("天气数据获取失败");
                }
            }
            
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onError("网络请求失败: " + t.getMessage());
            }
        });
    }
    
    private WeatherInfo convertToWeatherInfo(WeatherResponse response) {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setCity(response.getName());
        weatherInfo.setTemperature(response.getMain().getTemp());
        weatherInfo.setDescription(response.getWeather().get(0).getDescription());
        weatherInfo.setHumidity(response.getMain().getHumidity());
        weatherInfo.setWindSpeed(response.getWind().getSpeed() + " m/s");
        weatherInfo.setWeatherIcon(response.getWeather().get(0).getIcon());
        weatherInfo.setUpdateTime(System.currentTimeMillis());
        
        // 检查是否有预警信息
        checkWeatherWarnings(weatherInfo, response);
        
        return weatherInfo;
    }
    
    private void checkWeatherWarnings(WeatherInfo weatherInfo, WeatherResponse response) {
        // 简单的预警逻辑
        double temp = response.getMain().getTemp();
        int humidity = response.getMain().getHumidity();
        
        if (temp > 35) {
            weatherInfo.setHasWarning(true);
            weatherInfo.setWarningMessage("高温预警：气温将超过35°C，请注意防暑降温");
        } else if (temp < 0) {
            weatherInfo.setHasWarning(true);
            weatherInfo.setWarningMessage("低温预警：气温将低于0°C，请注意保暖防冻");
        } else if (humidity > 80) {
            weatherInfo.setHasWarning(true);
            weatherInfo.setWarningMessage("湿度过高，请注意通风除湿");
        }
    }
}