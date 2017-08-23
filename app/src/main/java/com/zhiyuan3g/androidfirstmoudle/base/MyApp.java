package com.zhiyuan3g.androidfirstmoudle.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LitePal
        LitePal.initialize(this);
    }
}
