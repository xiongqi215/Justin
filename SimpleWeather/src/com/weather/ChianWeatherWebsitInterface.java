package com.weather;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.json.JSONObject;
import android.util.Log;

import com.weather.util.http.HttpClientSupport;
import com.wheather.util.TimeUtil;

//example:
//{"weatherinfo":{"city":"长沙","city_en":"changsha","date_y":"2014年1月23日",
//	"date":"","week":"星期四","fchh":"11","cityid":"101250101","temp1":"17℃~6℃",
//	"temp2":"19℃~8℃","temp3":"19℃~9℃","temp4":"16℃~4℃","temp5":"17℃~7℃",
//	"temp6":"18℃~8℃","tempF1":"62.6℉~42.8℉","tempF2":"66.2℉~46.4℉","tempF3":"66.2℉~48.2℉",
//	"tempF4":"60.8℉~39.2℉","tempF5":"62.6℉~44.6℉","tempF6":"64.4℉~46.4℉","weather1":"晴",
//	"weather2":"晴转多云","weather3":"多云转阴","weather4":"多云","weather5":"晴","weather6":"多云",
//	"img1":"0","img2":"99","img3":"0","img4":"1","img5":"1","img6":"2","img7":"1","img8":"99","img9":"0",
//	"img10":"99","img11":"1","img12":"99","img_single":"0","img_title1":"晴","img_title2":"晴","img_title3":"晴",
//	"img_title4":"多云","img_title5":"多云","img_title6":"阴","img_title7":"多云","img_title8":"多云","img_title9":"晴",
//	"img_title10":"晴","img_title11":"多云","img_title12":"多云","img_title_single":"晴","wind1":"南风小于3级",
//	"wind2":"南风小于3级","wind3":"北风小于3级","wind4":"北风小于3级","wind5":"北风转南风小于3级","wind6":"南风小于3级",
//	"fx1":"南风","fx2":"南风","fl1":"小于3级","fl2":"小于3级","fl3":"小于3级","fl4":"小于3级","fl5":"小于3级","fl6":"小于3级",
//	"index":"较冷","index_d":"建议着大衣、呢外套加毛衣、卫衣等服装。体弱者宜着厚外套、厚毛衣。因昼夜温差较大，注意增减衣服。",
//	"index48":"较冷","index48_d":"建议着大衣、呢外套加毛衣、卫衣等服装。体弱者宜着厚外套、厚毛衣。因昼夜温差较大，注意增减衣服。",
//	"index_uv":"中等","index48_uv":"中等","index_xc":"适宜","index_tr":"适宜","index_co":"舒适","st1":"16","st2":"8","st3":"18",
//	"st4":"9","st5":"19","st6":"8","index_cl":"适宜","index_ls":"适宜","index_ag":"极不易发"}}
public class ChianWeatherWebsitInterface implements WeatherTransport {
	// 中国天气网银城市简单天气查询
	private final static String API_FOR_TODAY_WEATHER_INFO = "http://www.weather.com.cn/data/cityinfo/%s.html";
	// 中国天气网银城市实况天气查询
	private final static String API_FOR_CURRENT_WEATHER_INFO = "http://www.weather.com.cn/data/sk/%s.html";
	// 中国天气网城市天气详细查询接口
	private final static String API_FOR_WEATHER_INFO = "http://m.weather.com.cn/data/%s.html";
	// 中国天气网IP定位查询
	private final static String API_FOR_location = "http://61.4.185.48:81/g/";
	private HttpClientSupport httpClient;

	private WeatherPictureManager wpm;
	private static ChianWeatherWebsitInterface instance;
	private final String TAG = this.getClass().getSimpleName();

	private ChianWeatherWebsitInterface() {
		httpClient = new HttpClientSupport();
		wpm = new WeatherPictureManager();
	}

	public static ChianWeatherWebsitInterface getInstance() {
		if (instance == null) {
			instance = new ChianWeatherWebsitInterface();
		}
		return instance;
	}

	@Override
	public Weather getWeatherByCityCode(String cityCode) {
		Weather weather = null;
		String url;

		try {
			weather = new Weather();
			url = String.format(API_FOR_CURRENT_WEATHER_INFO, cityCode);
			HttpResponse response = httpClient.doGet(url);
			String result = httpClient.parseResponse(response);
			Log.i(TAG, result);
			JSONObject object = new JSONObject(result)
					.getJSONObject("weatherinfo");
			weather.setTemp_current(object.getString("temp"));
			weather.setUpdate_time(object.getString("time"));
			weather.setWind(object.getString("WD") + object.getString("WS"));
			weather.setHumidity(object.getString("SD"));
			weather.setDate(new Date().getTime());

			url = String.format(API_FOR_WEATHER_INFO, cityCode);
			response = httpClient.doGet(url);

			result = httpClient.parseResponse(response);
			Log.i(TAG, result);

			object = new JSONObject(result).getJSONObject("weatherinfo");
			weather.setCity(object.getString("city"));
			weather.setTemp_today(object.getString("temp1"));
			weather.setWeather_today(object.getString("weather1"));
			weather.setSuggest_dress(object.getString("index48_d"));
			weather.setWeather_icon(wpm.getWeatherIcon(object
					.getString("weather1")));

			WeatherTrend trend = new WeatherTrend(weather);
			for(int i=1;i<=6;i++){
				trend.addWeather(object.getString("weather"+i),
						object.getString("temp"+i),
						wpm.getWeatherIcon(object.getString("weather"+i)),
						TimeUtil.getDateAfterDays(weather.getDate(), i-1));
			}
			weather.setTrend(trend);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			httpClient.releaseConnection();
		}

		return weather;
	}

	public String getCityCode() throws ParseException, IOException,
			URISyntaxException {
		HttpResponse response = httpClient.doGet(API_FOR_location);
		String result = httpClient.parseResponse(response);
		String[] ss = result.split(";");
		String cityCodeProperty = ss[1];

		return cityCodeProperty.split("=")[1];
	}
}
