package com.weather.other;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	private List<WeatherTrendFr> fragments;

	public WeatherPagerAdapter(FragmentManager fm, List<WeatherTrendFr> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public WeatherTrendFr getItem(int position) {

		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
