package com.coolweather.app.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.coolweather.model.City;
import com.coolweather.app.coolweather.model.CoolWeatherDB;
import com.coolweather.app.coolweather.model.County;
import com.coolweather.app.coolweather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    /**
     * 解析服务器返回的JSON数据,并将解析出来的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            Log.d("response", response);
            JSONObject responseObject = new JSONObject(response);
            JSONArray heWeather = responseObject.getJSONArray("HeWeather data service 3.0");
            JSONObject jsonObject = heWeather.getJSONObject(0);
            JSONObject city = jsonObject.getJSONObject("basic");
            String cityName = city.getString("city");
            String weatherCode = city.getString("id");
            JSONArray daily = jsonObject.getJSONArray("daily_forecast");
            JSONObject weatherInfo = daily.getJSONObject(0);
            String publishTime = weatherInfo.getString("date");
            JSONObject condObject = weatherInfo.getJSONObject("cond");
            String weatherDesp = condObject.getString("txt_d");
            JSONObject tmpObject = weatherInfo.getJSONObject("tmp");
            String temp1 = tmpObject.getString("min");
            String temp2 = tmpObject.getString("max");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                       String temp1, String temp2, String weatherDesp,
                                       String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_data", sdf.format(new Date()));
        editor.commit();
    }
}
