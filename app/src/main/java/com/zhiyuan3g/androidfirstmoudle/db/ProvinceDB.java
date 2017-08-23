package com.zhiyuan3g.androidfirstmoudle.db;

import org.litepal.crud.DataSupport;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class ProvinceDB extends DataSupport{
    private int id;
    private String name;

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
}
