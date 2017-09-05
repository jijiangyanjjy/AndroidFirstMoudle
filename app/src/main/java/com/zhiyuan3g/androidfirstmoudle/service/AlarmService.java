package com.zhiyuan3g.androidfirstmoudle.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class AlarmService extends Service {

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("服务已开启");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //构建定时任务管理器对象
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //设置定时时间间隔
        long min = 8 * 60 * 60 * 1000;
        //设置定时时间
        long time = SystemClock.elapsedRealtime() + min;
        //设置意图对象
        Intent intent1 = new Intent(this, AlarmService.class);
        //设置延迟意图
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
        //开始定时任务
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    public AlarmService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
