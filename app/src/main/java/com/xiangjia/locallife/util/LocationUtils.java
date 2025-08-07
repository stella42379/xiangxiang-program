package com.xiangjia.locallife.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

/**
 * 定位工具类
 * 提供定位相关功能
 */
public class LocationUtils {
    
    private static final String TAG = "LocationUtils";
    
    // 定位相关常量
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    public static final long MIN_TIME_BETWEEN_UPDATES = 10000; // 10秒
    public static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10米
    
    private LocationManager locationManager;
    private LocationListener locationListener;
    private OnLocationListener onLocationListener;
    
    public LocationUtils(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
    
    /**
     * 检查定位权限
     */
    public static boolean checkLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * 检查GPS是否开启
     */
    public boolean isGPSEnabled() {
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    /**
     * 检查网络定位是否可用
     */
    public boolean isNetworkLocationEnabled() {
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    
    /**
     * 获取最后已知位置
     */
    public Location getLastKnownLocation() {
        Location bestLocation = null;
        
        if (locationManager != null) {
            // 获取GPS最后位置
            if (isGPSEnabled()) {
                Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (gpsLocation != null) {
                    bestLocation = gpsLocation;
                }
            }
            
            // 获取网络最后位置
            if (isNetworkLocationEnabled()) {
                Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (networkLocation != null) {
                    if (bestLocation == null || networkLocation.getAccuracy() < bestLocation.getAccuracy()) {
                        bestLocation = networkLocation;
                    }
                }
            }
        }
        
        return bestLocation;
    }
    
    /**
     * 开始定位
     */
    public void startLocationUpdates(Context context) {
        if (!checkLocationPermission(context)) {
            Log.w(TAG, "Location permission not granted");
            return;
        }
        
        if (locationManager == null) {
            Log.w(TAG, "LocationManager is null");
            return;
        }
        
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (onLocationListener != null) {
                    onLocationListener.onLocationChanged(location);
                }
            }
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (onLocationListener != null) {
                    onLocationListener.onStatusChanged(provider, status);
                }
            }
            
            @Override
            public void onProviderEnabled(String provider) {
                if (onLocationListener != null) {
                    onLocationListener.onProviderEnabled(provider);
                }
            }
            
            @Override
            public void onProviderDisabled(String provider) {
                if (onLocationListener != null) {
                    onLocationListener.onProviderDisabled(provider);
                }
            }
        };
        
        try {
            // 请求GPS定位
            if (isGPSEnabled()) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener
                );
            }
            
            // 请求网络定位
            if (isNetworkLocationEnabled()) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener
                );
            }
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException when requesting location updates", e);
        }
    }
    
    /**
     * 停止定位
     */
    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
    }
    
    /**
     * 计算两点之间的距离（米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }
    
    /**
     * 计算两点之间的距离（米）
     */
    public static double calculateDistance(Location location1, Location location2) {
        return location1.distanceTo(location2);
    }
    
    /**
     * 格式化距离显示
     */
    public static String formatDistance(double distanceInMeters) {
        if (distanceInMeters < 1000) {
            return String.format("%.0f米", distanceInMeters);
        } else {
            return String.format("%.1f公里", distanceInMeters / 1000);
        }
    }
    
    /**
     * 设置定位监听器
     */
    public void setOnLocationListener(OnLocationListener listener) {
        this.onLocationListener = listener;
    }
    
    /**
     * 定位监听器接口
     */
    public interface OnLocationListener {
        void onLocationChanged(Location location);
        void onStatusChanged(String provider, int status);
        void onProviderEnabled(String provider);
        void onProviderDisabled(String provider);
    }
    
    /**
     * 获取定位精度描述
     */
    public static String getAccuracyDescription(float accuracy) {
        if (accuracy <= 5) {
            return "高精度";
        } else if (accuracy <= 20) {
            return "中等精度";
        } else {
            return "低精度";
        }
    }
    
    /**
     * 检查位置是否有效
     */
    public static boolean isValidLocation(Location location) {
        return location != null && 
               location.getLatitude() != 0 && 
               location.getLongitude() != 0 &&
               location.getAccuracy() > 0;
    }
}
