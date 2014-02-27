package com.weather.other;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simple.weather.R;
import com.weather.util.db.City;

public class FavoriteCityAdpater extends BaseAdapter {
	private LayoutInflater inflater;
	private List<City> cityList;
    public FavoriteCityAdpater(Context context,List<City> cityList){
    	this.inflater=LayoutInflater.from(context);
    	this.cityList=cityList;
    }
	@Override
	public int getCount() {
		return cityList.size();
	}

	@Override
	public Object getItem(int position) {
		return cityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=this.inflater.inflate(R.layout.favorite_city_item, null);
		City city=this.cityList.get(position);
		TextView city_name=(TextView) convertView.findViewById(R.id.favorite_city_name);
		city_name.setText(city.getCity());
		return convertView;
	}

}
