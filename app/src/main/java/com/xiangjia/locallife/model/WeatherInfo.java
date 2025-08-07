package com.xiangjia.locallife.model;

public class WeatherInfo {
    private String city;
    private double temperature;
    private String description;
    private int humidity;
    private String windSpeed;
    private String weatherIcon;
    private long updateTime;
    private boolean hasWarning;
    private String warningMessage;
    
    public WeatherInfo() {}
    
    // Getters and setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    
    public String getWindSpeed() { return windSpeed; }
    public void setWindSpeed(String windSpeed) { this.windSpeed = windSpeed; }
    
    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
    
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    
    public boolean isHasWarning() { return hasWarning; }
    public void setHasWarning(boolean hasWarning) { this.hasWarning = hasWarning; }
    
    public String getWarningMessage() { return warningMessage; }
    public void setWarningMessage(String warningMessage) { this.warningMessage = warningMessage; }
}