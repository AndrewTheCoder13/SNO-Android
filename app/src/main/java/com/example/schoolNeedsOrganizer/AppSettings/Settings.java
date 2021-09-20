package com.example.schoolNeedsOrganizer.AppSettings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Settings {
    private static boolean notificationsStatus;
    private static String time;
    public static final String APP_PREFERENCES = "mysettings";
    private static TreeMap<Integer, String> weekDays;

    public static TreeMap<Integer, String> getWeekDays() {
        return weekDays;
    }

    public static void setWeekDays(TreeMap<Integer, String> weekDays, Context applicationContext) {
        Settings.weekDays = weekDays;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        List<String> list = new ArrayList<>();
        for(String week : weekDays.values()){
            list.add(week);
        }
        Gson gson = new Gson();
        editor.putString("week", gson.toJson(list));
        editor.apply();
    }

    public static boolean isNotificationsStatus() {
        return notificationsStatus;
    }

    public static void setNotificationsStatus(boolean notificationsStatus2, Context applicationContext) {
        notificationsStatus = notificationsStatus2;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.apply();
    }
    public static String getTime() {
        return time;
    }

    public static void setTime(String time2, Context applicationContext) {
        time = time2;
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("nTime", time);
        editor.putInt("nSend", -1);
        editor.apply();
    }

    public static void getSettings(Context applicationContext) {
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        notificationsStatus = mSettings.getBoolean("nStatus", false);
        time = mSettings.getString("nTime", "9:00");
        String string = mSettings.getString("week", "");
        weekDays = new TreeMap<>();
        if(string.equals("")){
            for(int j = 0; j < 7; j++){
                    weekDays.put(j, "");
            }
        } else {
            int i = 0;
            Gson gson = new Gson();
            List<String> list = gson.fromJson(string, ArrayList.class);
            for (String s : list) {
                weekDays.put(i, s);
                i++;
            }
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.putString("nTime", time);
        editor.putString("week", string);
        editor.apply();
    }

    public static void saveSettings(Context applicationContext){
        SharedPreferences mSettings = applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("nStatus", notificationsStatus);
        editor.putString("nTime", time);
        List<String> list = new ArrayList<>();
        for(String week : weekDays.values()){
            list.add(week);
        }
        Gson gson = new Gson();
        editor.putString("week", gson.toJson(list));
        editor.apply();
    }
}
