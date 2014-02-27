package com.weather;


import android.os.Handler;
import android.os.Message;

import com.simple.weather.R;

public class WeatherThread extends Thread {
    private Weather weather;
    private String cityCode;
    private Handler weatehrHandler;
    public WeatherThread(Handler weatehrHandler){
    this.weatehrHandler=weatehrHandler;
    }
    public WeatherThread(Handler weatehrHandler,String cityCode){
        this.weatehrHandler=weatehrHandler;
        this.cityCode=cityCode;
        }
    @Override
	public void run() {
       	try {
       		WeatherTransport wt= ChianWeatherWebsitInterface.getInstance();
       		if(null!=cityCode&&!"".equals(cityCode)&&!"N/A".equals(cityCode)){
       			weather=wt.getWeatherByCityCode(cityCode);
       		}else{
			weather=wt.getWeatherByCityCode("101010100");
			}
       		if(null==weather){
       			weatehrHandler.sendEmptyMessage(R.key.get_weather_fail);
       		}
       	} catch (Exception e) {
			e.printStackTrace();
			weatehrHandler.sendEmptyMessage(R.key.get_weather_fail);
			return;
		}   
			Message message=Message.obtain(weatehrHandler, R.key.update_weather, weather);
			weatehrHandler.sendMessage(message);
	}
}
