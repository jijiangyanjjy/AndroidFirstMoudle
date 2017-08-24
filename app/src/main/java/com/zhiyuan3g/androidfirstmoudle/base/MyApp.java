package com.zhiyuan3g.androidfirstmoudle.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class MyApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LitePal
        context = getApplicationContext();
        LitePal.initialize(this);
    }

    public static Context getContext(){
        return context;
    }
}
