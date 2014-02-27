package com.weather.other;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.weather.R;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;
import com.wheather.util.T;

public class SearchAdapter extends BaseAdapter implements Filterable{
	private List<City> allCities;
	private LayoutInflater inflater;
	private List<City> resultList;
	 private FavoriteCityHandler favoriteCityHandler;
	public SearchAdapter(Context context,List<City> allCities){
		inflater = LayoutInflater.from(context);
		this.allCities=allCities;
		this.resultList=new ArrayList<City>();
	}
	@Override
	public int getCount() {
		return resultList.size();
	}
	@Override
	public City getItem(int position) {
		
		return resultList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void setFavoriteCityHandler(FavoriteCityHandler favoriteCityHandler) {
		this.favoriteCityHandler = favoriteCityHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=this.inflater.inflate(R.layout.search_city_item, null);
		final City city=this.resultList.get(position);
		TextView province=(TextView) convertView.findViewById(R.id.search_province);
		province.setText(city.getProvince());
		TextView cityName=(TextView) convertView.findViewById(R.id.search_city);
		cityName.setText(city.getCity());
		ImageView favorite=(ImageView) convertView.findViewById(R.id.btn_sc);
		if(city.isFavorite()){
			favorite.setImageResource(R.drawable.sc);
		}
		favorite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(favoriteCityHandler!=null){
					boolean isFavority=(city.isFavorite())?false:true;
					favoriteCityHandler.setFavority(city, isFavority);
					if(isFavority){
						 ((ImageView)v).setImageResource(R.drawable.sc);
					}else{
						 ((ImageView)v).setImageResource(R.drawable.sc1);
					}
				}
				
			}
		});
		return convertView;
	}
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			
			@Override
			protected FilterResults performFiltering(CharSequence s) {
				String str = s.toString().toUpperCase();
				FilterResults results = new FilterResults();
				ArrayList<City> cityList = new ArrayList<City>();
				if (cityList != null && allCities.size() != 0) {
					for (City cb : allCities) {
						// 匹配全屏、首字母、和城市名中文
						if (cb.getAllFristPY().indexOf(str) > -1
								|| cb.getAllPY().indexOf(str) > -1
								|| cb.getCity().indexOf(str) > -1) {
							cityList.add(cb);
						}
					}
				}
				results.values = cityList;
				results.count = cityList.size();
				return results;
			}
			
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				resultList = (ArrayList<City>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

		};
		return filter;
	}

}
