package com.weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherTrend implements Serializable {
	private static final long serialVersionUID = -5722754431337481101L;
private List<Weather> trend;
private Weather base;
public WeatherTrend(Weather base){
	trend=new ArrayList<Weather>();
	this.base=base;
}

public void addWeather(String weather,String temp,int weatehr_icon,Date weatherDate){
	Weather w=new Weather();
	w.setCity(base.getCity());
	w.setCity_id(base.getCity_id());
	w.setTemp_today(temp);
	w.setWeather_today(weather);
	w.setWeather_icon(weatehr_icon);
	w.setDate(weatherDate.getTime());
	this.trend.add( w);
}

public List<Weather> getTrend() {
	return trend;
}

}
