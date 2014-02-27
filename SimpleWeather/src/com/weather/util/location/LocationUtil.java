package com.weather.util.location;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.simple.weather.R;
import com.wheather.util.LogUtil;
import com.wheather.util.NetUtil;
import com.wheather.util.T;
/**
 * 使用百度locSDK_3.3.jar定位
 * @author Justin
 *
 */
public class LocationUtil {
	private Context context;
	private LocationClient mLocationClient;
	private String cityName;
    private Handler wh;
	public LocationUtil(Context context,Handler wh){
		this.context=context;
		this.wh=wh;
		
		init();
	}
    public void init(){
    	 mLocationClient = new LocationClient(this.context);     //声明LocationClient类
    	 mLocationClient.setLocOption(this.getLocationClientOption());
    	 mLocationClient.registerLocationListener( mLocationListener );    //注册监听函数
    }
	public void getLocation(){
		
		if(mLocationClient!=null){
			if(!mLocationClient.isStarted()){
				mLocationClient.start();
				T.showShort(context, R.string.locating);
			}
			mLocationClient.requestLocation();
			
		}
	}
	BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null ||location.getCity()==null) {
			
				wh.sendEmptyMessage(R.key.locate_fail);
				return;
			}
//				
			 cityName =  location.getCity();
			
			LogUtil.i(cityName);
			mLocationClient.stop();
			
			Message msg = wh.obtainMessage();
			msg.what = R.key.locate_ok;
			msg.obj = cityName;
			wh.sendMessage(msg);// 定位成功，更新天气
		}
	};
	
	private LocationClientOption getLocationClientOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setServiceName(this.getClass().getPackage().getName());
		option.setScanSpan(0);
		option.disableCache(true);
		return option;
	}
	
	
	public String getCityName() {
		return cityName;
	}
	
}
