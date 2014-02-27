package com.weather.util.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.wheather.util.LogUtil;
import com.wheather.util.T;

public class CityDBExcuter {
	public static final String CITY_DB_NAME = "city.db";
	private static final String CITY_TABLE_NAME = "city";
	private static final String path="/data"
			+ Environment.getDataDirectory().getAbsolutePath()
			+ File.separator + "com.simple.weather" + File.separator
			+ CITY_DB_NAME;
	private SQLiteDatabase db;
	
	public CityDBExcuter(Context context){
		
		File dbFile = new File(path);
		if (!dbFile.exists()) {
			try {
				InputStream is = context.getAssets().open(CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(dbFile);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				T.showLong(context, e.getMessage());
				System.exit(0);
			}
		}
			db=context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
		
	}
	
	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("city"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("allpy"));
			String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
			String firstPY = c.getString(c.getColumnIndex("firstpy"));
			String isFavority=c.getString(c.getColumnIndex("isfavorite"));
		
			City item = new City(province, city, number, firstPY, allPY,
					allFirstPY,("1".equals(isFavority)?true:false));
			list.add(item);
		}
		return list;
	}
	
	public List<City> getFavoireCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME+" where isfavorite=?", new String[]{"1"});
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("city"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("allpy"));
			String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
			String firstPY = c.getString(c.getColumnIndex("firstpy"));
			String isFavority=c.getString(c.getColumnIndex("isfavorite"));
		
			City item = new City(province, city, number, firstPY, allPY,
					allFirstPY,("1".equals(isFavority)?true:false));
			list.add(item);
		}
		return list;
	}


	public City getCity(String city) {
		if(TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(city);
		if (item == null) {
			item = getCityInfo(parseName(city));
		}
		return item;
	}
	
	public String getCityCode(String city) {
		if(TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(parseName(city));
		if (item == null) {
			item = getCityInfo(city);
		}
		return item.getNumber();
	}
	
	/**
	 * 去掉市或县搜索
	 * @param city
	 * @return
	 */
	private String parseName(String city) {
		if (city.contains("市")) {// 如果为空就去掉市字再试试
			String subStr[] = city.split("市");
			city = subStr[0];
		} else if (city.contains("县")) {// 或者去掉县字再试试
			String subStr[] = city.split("县");
			city = subStr[0];
		}
		return city;
	}

	private City getCityInfo(String city) {
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where city=?", new String[] { city });
		if (c.moveToFirst()) {
			String province = c.getString(c.getColumnIndex("province"));
			String name = c.getString(c.getColumnIndex("city"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("allpy"));
			String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
			String firstPY = c.getString(c.getColumnIndex("firstpy"));
			String isFavority=c.getString(c.getColumnIndex("isfavorite"));
			City item = new City(province, name, number, firstPY, allPY,
					allFirstPY,("1".equals(isFavority)?true:false));
			return item;
		}
		return null;
	}
	
	public void setFavoriteCity(String cityCode,boolean isfavortie){
		String result=(isfavortie)?"1":"0";
		ContentValues values = new ContentValues();
		values.put("ISFAVORITE",result);//key为字段名，value为值
		
		db.update(CITY_TABLE_NAME, values, "NUMBER=?", new String[]{cityCode});
		
	} 
	
	public void close(){
		if(this.db!=null){
			this.db.close();
		}
		
	}
}
