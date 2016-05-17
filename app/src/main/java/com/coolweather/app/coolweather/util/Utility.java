package com.coolweather.app.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.coolweather.model.City;
import com.coolweather.app.coolweather.model.CoolWeatherDB;
import com.coolweather.app.coolweather.model.County;
import com.coolweather.app.coolweather.model.Province;

/**
 * Created by syt on 16/5/16.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,
                                                              String response) {
        if(!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if(allProvince != null && allProvince.length > 0) {
                for(String p : allProvince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到PROVINCE表
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB , String response,
                                               int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            Log.d("Utility", "succeed!");
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0) {
                for(String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //解析出来的数据存储到city表
                    coolWeatherDB.saveCity(city);
                }
            }
            return true;
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response,
                                                 int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0) {

                for(String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    //将解析出来的数据存储到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
