package com.weather.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simple.weather.R;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;

import android.content.Context;
import android.os.Handler;

public class CityList {
	private static final String FORMAT = "^[a-z,A-Z].*$";
	private static final String DEF_CITY_GROUP_NAME = "#";
	private Handler handler;
	private boolean isFinish=false;
	public boolean isFinish() {
		return isFinish;
	}

	private List<City> cityList;
	// 首字母集
	private List<String> sections;
	// 根据首字母存放数据
	private Map<String, List<City>> pinyinMap;
	// 首字母位置集
	private List<Integer> positions;
	// 首字母对应的位置
	private Map<String, Integer> indexer;
	private Context context;
	private CityDBExcuter excuter ;

	public CityList(Context context,Handler handler,CityDBExcuter excuter ) {
		this.context = context;
		 this.excuter = excuter;
		this.handler=handler;
		sections = new ArrayList<String>();
		pinyinMap = new HashMap<String, List<City>>();
		positions = new ArrayList<Integer>();
		indexer = new HashMap<String, Integer>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				isFinish=false;
				orgCityList();
			}
		}).start();
		
	}

	public void orgCityList() {
		cityList = excuter.getAllCity();
		for (City city : cityList) {
			String fristPinyin = city.getFirstPY();
			if (fristPinyin.matches(FORMAT)) {
				if (!sections.contains(fristPinyin)) {
					sections.add(fristPinyin);
					List<City> list = new ArrayList<City>();
					list.add(city);
					pinyinMap.put(fristPinyin, list);
				} else {
					pinyinMap.get(fristPinyin).add(city);
				}
			} else {
				if (!sections.contains(DEF_CITY_GROUP_NAME)) {
					sections.add(fristPinyin);
					List<City> list = new ArrayList<City>();
					list.add(city);
					pinyinMap.put(DEF_CITY_GROUP_NAME, list);
				} else {
					pinyinMap.get(DEF_CITY_GROUP_NAME).add(city);
				}

			}
		}
		Collections.sort(sections);
		int position = 0;
		for (int i = 0; i < sections.size(); i++) {
			indexer.put(sections.get(i), position);
			positions.add(position);
			position += pinyinMap.get(sections.get(i)).size();
		}
		isFinish=true;
		handler.sendEmptyMessage(R.key.city_list_finish);
	}

	public List<City> getCityList() {
		return cityList;
	}

	public List<String> getSections() {
		return sections;
	}

	public Map<String, List<City>> getPinyinMap() {
		return pinyinMap;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public Map<String, Integer> getIndexer() {
		return indexer;
	}

	public Context getContext() {
		return context;
	}
	
}
