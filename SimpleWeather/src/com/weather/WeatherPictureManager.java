package com.weather;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.simple.weather.R;


public class WeatherPictureManager {
	
	private Map<String,Integer> weatherIcon;
	public WeatherPictureManager(){
		initWeatherIcon();
	}
//	public void initWeatherIcon(){
//		weatherIcon = new HashMap<String, Integer>();
//		weatherIcon.put("暴雪", R.drawable.snow);
//		weatherIcon.put("暴雨", R.drawable.rain);
//		weatherIcon.put("大暴雨", R.drawable.rain);
//		weatherIcon.put("大雪", R.drawable.snow);
//		weatherIcon.put("大雨", R.drawable.rain);
//
//		weatherIcon.put("多云", R.drawable.cloudy);
//		weatherIcon.put("雷阵雨", R.drawable.leizhengyu);
//		weatherIcon.put("雷阵雨冰雹",R.drawable.lenzhenbinbao);
//		weatherIcon.put("晴", R.drawable.sun);
////		weatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);
//
//		weatherIcon.put("特大暴雨", R.drawable.rain);
//		weatherIcon.put("雾", R.drawable.fog);
//		weatherIcon.put("小雪", R.drawable.snow);
//		weatherIcon.put("小雨", R.drawable.rain);
//		weatherIcon.put("阴", R.drawable.heavy_cloudy);
//
//		weatherIcon.put("雨夹雪", R.drawable.snow);
//		weatherIcon.put("阵雪", R.drawable.snow);
//		weatherIcon.put("阵雨", R.drawable.rain);
//		weatherIcon.put("中雪", R.drawable.snow);
//		weatherIcon.put("中雨", R.drawable.rain);
//	}
	
	private void initWeatherIcon() {
		weatherIcon = new HashMap<String, Integer>();
		weatherIcon.put("暴雪", R.drawable.w17);
		weatherIcon.put("暴雨", R.drawable.w10);
		weatherIcon.put("大暴雨", R.drawable.w10);
		weatherIcon.put("大雪", R.drawable.w16);
		weatherIcon.put("大雨", R.drawable.w9);
		weatherIcon.put("多云", R.drawable.w1);
		weatherIcon.put("雷阵雨", R.drawable.w4);
		weatherIcon.put("雷阵雨冰雹", R.drawable.w19);
		weatherIcon.put("晴", R.drawable.w0);
		weatherIcon.put("沙尘暴", R.drawable.w20);
		weatherIcon.put("特大暴雨", R.drawable.w10);
		weatherIcon.put("雾", R.drawable.w18);
		weatherIcon.put("小雪", R.drawable.w14);
		weatherIcon.put("小雨", R.drawable.w7);
		weatherIcon.put("阴", R.drawable.w2);
		weatherIcon.put("雨夹雪", R.drawable.w6);
		weatherIcon.put("阵雪", R.drawable.w13);
		weatherIcon.put("阵雨", R.drawable.w3);
		weatherIcon.put("中雪", R.drawable.w15);
		weatherIcon.put("中雨", R.drawable.w8);
		weatherIcon.put("霾", R.drawable.w20);
	}
	
	public int getWeatherIcon(String climate) {
		int weatherRes = R.drawable.sun;
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		String[] strs = { "晴", "晴" };
		if (climate.contains("转")) {// 天气带转字，取前面那部分
			strs = climate.split("转");
			climate = strs[0];
			if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = climate.split("到");
				climate = strs[1];
			}
		}
		if (weatherIcon.containsKey(climate)) {
			weatherRes = weatherIcon.get(climate);
		}
		return weatherRes;
	}

}
