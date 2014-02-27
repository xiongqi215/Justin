package com.weather.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.weather.R;
import com.weather.Weather;
import com.weather.WeatherPictureManager;
import com.wheather.util.TimeUtil;

public class WeatherTrendFr extends Fragment{
    private View view;
    private ArrayList<View> childView;
    public WeatherTrendFr(){
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.weather_trend, container,false);
		childView=new ArrayList<View>();
		View item1=view.findViewById(R.id.day1);
		View item2=view.findViewById(R.id.day2);
		View item3=view.findViewById(R.id.day3);
		childView.add(item1);
		childView.add(item2);
		childView.add(item3);
		
		return view;
	}
	
	public void updateWeather(List<Weather> trend,int beginIndex){
		int j=0;
		for(int i=beginIndex;i<=trend.size();i++){
			if(j>=childView.size()){
				return;
			}
			Weather weatherInfo=trend.get(i);
			View item1=childView.get(j);
			TextView date=(TextView) item1.findViewById(R.id.date);
			String dayName=TimeUtil.getDayName(weatherInfo.getDate());
			if(dayName.equals("今天")){
				date.setText(dayName);
			}else{
				date.setText(TimeUtil.getChinessWeek(weatherInfo.getDate()));
			}
			ImageView weather_icon=(ImageView) item1.findViewById(R.id.icon);
			TextView weather=(TextView) item1.findViewById(R.id.weather);
			weather.setText(weatherInfo.getWeather_today());
			weather_icon.setImageResource(weatherInfo.getWeather_icon());
			TextView temp=(TextView) item1.findViewById(R.id.temp);
			temp.setText(weatherInfo.getTemp_today());
			j++;
		}
		
	}
	
	
}
