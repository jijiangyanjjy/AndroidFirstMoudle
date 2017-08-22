package com.zhiyuan3g.androidfirstmoudle.utils;

import android.app.Activity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/9.
 */

public class OkHttpUtils {
    /**
     * @param activity       activity类
     * @param url            请求网址
     * @param okHttpCallBack 请求成功或失败的回调
     */
    public static void sendRequestGETMethod(final Activity activity, final String url, final OkHttpCallBack okHttpCallBack) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    final String result = response.body().string();
                    if (okHttpCallBack != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okHttpCallBack.Success(result);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (okHttpCallBack != null) {
                        okHttpCallBack.Failure("网络连接失败");
                    }
                }
            }
        }.start();
    }

    /**
     * @param activity activity类
     * @param url      请求网址
     * @param callBack 请求成功或失败的回调
     */
    public static void SendRequestPOSTMethod(final Activity activity, final String url, final RequestBody requestBody, final OkHttpCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .method("POST", requestBody)
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    final String result = response.body().string();
                    if (callBack != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.Success(result);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack != null) {
                                callBack.Failure("网络连接失败！");
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
