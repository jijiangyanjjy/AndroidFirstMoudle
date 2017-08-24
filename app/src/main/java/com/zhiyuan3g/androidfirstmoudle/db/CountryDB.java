package com.zhiyuan3g.androidfirstmoudle.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class CountryDB extends DataSupport {

    private int id;
    private int cityCode;
    private String name;
    private int countryCode;
    private String weather_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }
}
