package com.weather;

import java.io.Serializable;


public class Weather implements Serializable {

	private static final long serialVersionUID = -511836210860367094L;
private String city;
  private String city_id;
  private String temp_current;
  private String temp_today;
  private String weather_today;
  private String wind;
  private String humidity;
  private Integer weather_icon;
  private String suggest_dress;
  private String update_time;
  private long date;
  private WeatherTrend trend;
  public WeatherTrend getTrend() {
	return trend;
}
public void setTrend(WeatherTrend trend) {
	this.trend = trend;
}
public long getDate() {
	return date;
}
public void setDate(long date) {
	this.date = date;
}
public String getSuggest_dress() {
	return suggest_dress;
}
public void setSuggest_dress(String suggest_dress) {
	this.suggest_dress = suggest_dress;
}
public Integer getWeather_icon() {
	return weather_icon;
}
public void setWeather_icon(Integer weather_icon) {
	this.weather_icon = weather_icon;
}
public String getWind() {
	return wind;
}
public void setWind(String wind) {
	this.wind = wind;
}
public String getHumidity() {
	return humidity;
}
public void setHumidity(String humidity) {
	this.humidity = humidity;
}

public String getUpdate_time() {
	return update_time;
}
public void setUpdate_time(String update_time) {
	this.update_time = update_time;
}
public String getCity() {
	return city;
}
public String getCity_id() {
	return city_id;
}
public void setCity_id(String city_id) {
	this.city_id = city_id;
}
public String getTemp_today() {
	return temp_today;
}

public String getWeather_today() {
	return weather_today;
}

public void setCity(String city) {
	this.city = city;
}
public void setTemp_today(String temp_today) {
	this.temp_today = temp_today;
}

public void setWeather_today(String weather_today) {
	this.weather_today = weather_today;
}

public String getTemp_current() {
	return temp_current;
}
public void setTemp_current(String temp_current) {
	this.temp_current = temp_current;
	if(!this.temp_current.contains("℃")){
		this.temp_current+="℃";
	}
}
  public String SmsText(){
	  return this.city+this.weather_today+"，"+this.temp_today+"，\b\r"+
              "湿度"+this.humidity+"，"+this.wind+"，\b\r"+this.suggest_dress+"("+this.update_time+")";
  }
  
}
