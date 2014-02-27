package com.weather.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simple.weather.R;
import com.weather.Weather;
import com.weather.WeatherThread;
import com.weather.WeatherTrend;
import com.weather.other.WeatherPagerAdapter;
import com.weather.other.WeatherTrendFr;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;
import com.weather.util.location.LocationUtil;
import com.wheather.util.NetUtil;
import com.wheather.util.SharedPreferencesUtil;
import com.wheather.util.SharedPreferencesUtil.CityCache;
import com.wheather.util.SharedPreferencesUtil.WeatherCache;
import com.wheather.util.T;
import com.wheather.util.TimeUtil;

public class WeatherActivity extends FragmentActivity implements OnClickListener {
	private final static String Tag = WeatherActivity.class.getSimpleName();
	private TextView weather_city,temp_current,weather_today,update_time,wind,humidity,condition;
	private ImageView city_selector,weather_icon,share,reLocate,refresh_button;
	private WeatherThread wt;
	private String shareInfo;
    private ProgressBar updateProgressBar;
    private Weather curWeatherInfo;
    private City curCity;
    private LocationUtil lu;
	private SharedPreferencesUtil.WeatherCache weatherCache;
	private SharedPreferencesUtil.CityCache cityCache;
    private CityDBExcuter dbExcuter;
    private ViewPager weather_trend_view;
    private List<WeatherTrendFr> fragments;
    private WeatherPagerAdapter weatherTrendAdapter;
    
    private Handler handler = new Handler() {
    	@Override
		public void handleMessage(Message msg) {
    		Weather weather;
    		City city;
    		switch (msg.what) {
    		case R.key.get_cached_weather:
    			setUpdateState(R.key.update_start);
    			curCity=cityCache.getCachedCity();
    			if(curCity==null){
    				goToSelector();
    				return;
    			}
    			 curWeatherInfo=weatherCache.getCachedWeather();
    			 if(curWeatherInfo==null){
    				reloadWeather(curCity.getNumber()); 
    				return;
    			 }
    			 initWeather(curWeatherInfo);
    			break;
    		case R.key.update_weather:
    			// 更新天气信息
    			weather = (Weather) msg.obj;
    			if (weather != null) {
    				setCurWeatherInfo(weather);
    				initWeather(weather);
    				
    			}
    			break;
    		case R.key.change_city:
    			// 城市切换，重新加载天气
    			city = (City) msg.obj;
    			if (city != null) {
    				setUpdateState(R.key.update_start);
    				cityCache.cacheCity(city);
    				reloadWeather(city.getNumber());
    			}
    			break;
    			
    		case R.key.locate_ok:
    			// 定位成功
    			String cityName = (String) msg.obj;
    			T.showShort(WeatherActivity.this, R.string.location_sccuess+cityName);
    			city=dbExcuter.getCity(cityName);
    			setCurCity(city);
    			break;
    			
    		case R.key.get_weather_fail:
    			// 更新天气失败
    			T.showLong(WeatherActivity.this, R.string.get_weather_fail);
    			initWeather(curWeatherInfo);
    			break;
    		case R.key.locate_fail:
    			// 定位失败
    			T.showLong(WeatherActivity.this, R.string.locate_fail);
    			setUpdateState(R.key.update_finish);
    			break;
    		}
    		
    	}
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(Tag, "App on create!");
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.setContentView(R.layout.main_layout);
		 this.initViewGroup();
		 this. dbExcuter = new CityDBExcuter(this);
		 this.lu=new LocationUtil(this,handler);
		 weatherCache = new WeatherCache(this);
		 cityCache = new CityCache(this);
		 
		 new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(R.key.get_cached_weather);
				}

			}.start();
		 
	}
	
    public Weather getCurWeatherInfo() {
		return curWeatherInfo;
	}

	public void setCurWeatherInfo(Weather curWeatherInfo) {
		this.curWeatherInfo = curWeatherInfo;
		 this.weatherCache.cacheWeather(curWeatherInfo);
	}

	public City getCurCity() {
		return curCity;
	}

	public void setCurCity(City curCity) {
		this.curCity = curCity;
		Message message=this.handler.obtainMessage();
		message.what=R.key.change_city;
		message.obj=curCity;
		this.handler.sendMessage(message);
	}

	public void initViewGroup(){
    	weather_city=(TextView) this.findViewById(R.id.weather_city);
		weather_today=(TextView) this.findViewById(R.id.weather_today);
		temp_current=(TextView) this.findViewById(R.id.current_temp);
		update_time=(TextView) this.findViewById(R.id.update_time);
		city_selector=(ImageView) this.findViewById(R.id.city_selector);
		refresh_button=(ImageView) this.findViewById(R.id.btn_update);
		refresh_button.setOnClickListener(this);
		wind=(TextView) this.findViewById(R.id.wind);
		humidity=(TextView) this.findViewById(R.id.humidity);
		condition=(TextView) this.findViewById(R.id.condition);
		updateProgressBar=(ProgressBar) this.findViewById(R.id.update_progress);
		weather_icon=(ImageView) this.findViewById(R.id.weather_icon);
		
		reLocate=(ImageView) this.findViewById(R.id.btn_locate);
		reLocate.setOnClickListener(this);
		
		share=(ImageView) this.findViewById(R.id.btn_share);
		share.setOnClickListener(this);
		
		city_selector=(ImageView) this.findViewById(R.id.city_selector);
		city_selector.setOnClickListener(this);
		
		 this.fragments=new ArrayList<WeatherTrendFr>();
		 this.fragments.add(new WeatherTrendFr());
		 this.fragments.add(new WeatherTrendFr());
		weatherTrendAdapter = new WeatherPagerAdapter(
				getSupportFragmentManager(), fragments);
		weather_trend_view=(ViewPager) this.findViewById(R.id.weather_trend_view);
		weather_trend_view.setAdapter(weatherTrendAdapter);
		
    }
	@Override
	protected void onResume() {
		Log.d(Tag, "App on resume!");
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d(Tag, "App on destroy!");
		super.onDestroy();
		this.dbExcuter.close();
	}

	@Override
	protected void onPause() {
		Log.d(Tag, "App on pause!");
		super.onPause();
	}
	
	public void reloadWeather(String cityCode){
		 wt=new WeatherThread(this.handler,cityCode);
			wt.start();
			T.showShort(this, R.string.weather_loadding);
	}
	private void changeBackground(int picId){
		View main_view=findViewById(R.id.main_view);
		main_view.setBackgroundResource(picId);
	}
	public void initWeather(Weather weather){
		if(weather!=null){
		weather_city.setText(weather.getCity());
		weather_today.setText("今日："+weather.getTemp_today());
		condition.setText(weather.getWeather_today());
		temp_current.setText(weather.getTemp_current());
		update_time.setText(TimeUtil.getDayName(weather.getDate())+weather.getUpdate_time()+"发布");
		wind.setText(weather.getWind());
		humidity.setText("湿度"+weather.getHumidity());
		weather_icon.setImageResource(weather.getWeather_icon());
		shareInfo=weather.SmsText();
		initWeatherTrend();
		}
		this.setUpdateState(R.key.update_finish);
		
	}
	public void initWeatherTrend(){
		WeatherTrend trend=this.curWeatherInfo.getTrend();
		if(trend!=null){
		
				weatherTrendAdapter.getItem(0).updateWeather(trend.getTrend(), 0);
				weatherTrendAdapter.getItem(1).updateWeather(trend.getTrend(), 3);
		}
		this.weather_trend_view.setVisibility(View.VISIBLE);
	}

	public void goToSelector(){
		  	Intent intent=new Intent();
			intent.setClass(WeatherActivity.this, CitySelectActivity.class);
			startActivityForResult(intent, 0);
	  }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			City city = (City) data.getSerializableExtra("City");
			this.setCurCity(city);
		}
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_update:
			if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
				T.showShort(this, R.string.net_err);
				return;
			}
			this.update_time.setText(R.string.updating);
			this.setUpdateState(R.key.update_start);
			this.reloadWeather(this.curCity.getNumber());
			break;
		case R.id.btn_locate:
			this.setUpdateState(R.key.update_start);
			if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
				T.showShort(this, R.string.net_err);
				return;
			}
	         lu.getLocation();
			break;
		case R.id.btn_share:
			 Intent intent=new Intent(Intent.ACTION_SEND);   
		        intent.setType("text/plain");  
		        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
		        intent.putExtra(Intent.EXTRA_TEXT, shareInfo);  
		        intent.putExtra(Intent.EXTRA_TITLE, "简单天气");  
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		        startActivity(Intent.createChooser(intent, "请选择")); 
		        break;
		case R.id.city_selector:
			goToSelector();
			break;
		}
		
	}
	public void setUpdateState(int state){
		switch(state){
		case R.key.update_start:
			this.refresh_button.setVisibility(View.GONE);
			this.updateProgressBar.setVisibility(View.VISIBLE);
			break;
		case R.key.update_finish:
			this.refresh_button.setVisibility(View.VISIBLE);
			this.updateProgressBar.setVisibility(View.GONE);
			break;
		}
		
	}
}
