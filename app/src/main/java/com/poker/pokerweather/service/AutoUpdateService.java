package com.poker.pokerweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.poker.pokerweather.gson.Weather;
import com.poker.pokerweather.util.HttpUtil;
import com.poker.pokerweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Poker on 2017/10/11.
 */

public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    SharedPreferences prefs;

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        updateWeather();
        updateBingPic();
        String time = prefs.getString("list_update_time", null);
        if (time != null) {
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            int anHour = 0;
            switch (time) {
                case "0":
                    anHour = 60 * 60 * 1000;
                    break;
                case "1":
                    anHour = 3 * 60 * 60 * 1000;
                    break;
                case "2":
                    anHour = 8 * 60 * 60 * 1000;
                    break;
            }
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(this, AutoUpdateService.class);
            PendingIntent pi = PendingIntent.getService(this,0,i,0);
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        String weatherString = prefs.getString("weather", null);
        boolean isUpdateWeatherEnabled = prefs.getBoolean("check_box_auto_weather", true);
        if (weatherString != null && isUpdateWeatherEnabled) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String apiKey = prefs.getString("edit_text_api_key",null);
            if (apiKey == null) {
                apiKey = "1e1e74a94599427b9d896379e5f2e27f";
            }
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=" + apiKey;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updateBingPic() {
        boolean isUpdateBingEnabled = prefs.getBoolean("check_box_auto_bing",true);
        if (isUpdateBingEnabled) {
            String requestBingUrl = prefs.getString("edit_text_bing_api", null);
            if (requestBingUrl == null) {
                requestBingUrl = "http://guolin.tech/api/bing_pic";
            }
            HttpUtil.sendOkHttpRequest(requestBingUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String bingPic = response.body().string();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic", bingPic);
                    editor.apply();
                }
            });
        }
    }
}
