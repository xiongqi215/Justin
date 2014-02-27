package com.wheather.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.weather.Weather;
import com.weather.util.db.City;

public class SharedPreferencesUtil {
	public static final String WEATHER_INFO_FILE = "Weather";
	public static final String CITY_INFO = "City";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Context context;
	private static final String path="/data"
			+ Environment.getDataDirectory().getAbsolutePath()
			+ File.separator + "com.simple.weather" + File.separator;
	private final String TAG=SharedPreferencesUtil.class.getSimpleName();
	private SharedPreferencesUtil(Context context,String file){
		this.context=context;
		sp=context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor=sp.edit();
	}
	
	protected void putString(String key,String value){
		if(value==null){
			value="N/A";
		}
		editor.putString(key, value);
	}
	
	protected void putInt(String key, int value){
		editor.putInt(key, value);
	}
	
	protected void putLong(String key, long value){
		editor.putLong(key, value);
	}
	
	protected String getString(String key){
		return sp.getString(key, "N/A");
	}
	
	protected int getInt(String key){
		  return sp.getInt(key, 0);
	}
	
	protected long getLong(String key){
		  return sp.getLong(key, 0);
	}
	
	protected void putObject(String key,Object object){
		try {
			FileOutputStream fos=new FileOutputStream(path+key);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Object getObject(String key){
		Object object=null;
		FileInputStream fis=null;
		ObjectInputStream ois=null;
		File file=new File(this.path+key);
		if(!file.exists()){
			return null;
		}
		try{
		fis=new FileInputStream(file);
		 ois=new ObjectInputStream(fis);
		object=ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
			if(ois!=null){
				ois.close();
			}if(fis!=null){
				fis.close();
			}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return object;
	}
	
	protected void commit(){
		this.editor.commit();
	}
	
	
	
	 public static class WeatherCache extends SharedPreferencesUtil{
		public WeatherCache(Context context){
			super(context,WEATHER_INFO_FILE);
		}
		
		public Weather getCachedWeather(){
			Weather weather=(Weather) this.getObject("Cached_Weather");
//			weather=new Weather();
//			weather.setCity(this.getString("City"));
//			weather.setTemp_today(this.getString("Temp_Today"));
//			weather.setTemp_current(this.getString("Temp_Current"));
//			weather.setUpdate_time(this.getString("Update_Time"));
//			weather.setWind(this.getString("Wind"));
//			weather.setHumidity(this.getString("Humidity"));
//			weather.setSuggest_dress(this.getString("Suggest_dress"));
//			weather.setWeather_today(this.getString("Weather_today"));
//			weather.setWeather_icon(this.getInt("Weather_icon"));
//			weather.setDate(this.getLong("Weather_Date"));
			return weather;
		}
		
		public void cacheWeather(Weather weather){
           if(weather ==null){
        	   return;
           }
//			this.putString ("City", weather.getCity());
//			this.putString ("Temp_Today", weather.getTemp_today());
//			this.putString ("Temp_Current", weather.getTemp_current());
//			this.putString ("Update_Time", weather.getUpdate_time());
//			this.putString ("Wind", weather.getWind());
//			this.putString ("Humidity", weather.getHumidity());
//			this.putString ("Suggest_dress", weather.getSuggest_dress());
//			this.putString ("Weather_today", weather.getWeather_today());
//			this.putInt ("Weather_icon", weather.getWeather_icon());
//			this.putLong ("Weather_Date", weather.getDate());
           
           this.putObject("Cached_Weather",weather);
		}
		
	}
	 
	 public static class CityCache extends SharedPreferencesUtil{
			public CityCache(Context context){
				super(context,CITY_INFO);
			}
			public City getCachedCity(){
				City city=(City) getObject("Cached_City");
//				city.setCity(this.getString("City"));
//				city.setNumber(this.getString("CityCode"));
//				city.setAllFristPY(this.getString("AllFristPY"));
//				city.setAllFristPY(this.getString("FristPY"));
//				city.setProvince(this.getString("(Province)"));
//				city.setAllPY(this.getString("AllPY"));
				return city;
			}
			
			public void cacheCity(City city){
				if(city==null){
					return;
				}
				this.putObject("Cached_City", city);
			}
	 }
	
}
