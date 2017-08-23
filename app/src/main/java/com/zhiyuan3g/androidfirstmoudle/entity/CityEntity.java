package com.zhiyuan3g.androidfirstmoudle.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class CityEntity {

    private int id;
    private int cityCode;
    private String name;
    private int provinceCode;

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
