package com.weather.other;

import java.util.List;

import android.content.Context;

import com.simple.weather.R;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;
import com.wheather.util.T;

public class FavoriteCityHandler {
	private Context context;
	private CityDBExcuter excuter;
 public FavoriteCityHandler(Context context,CityDBExcuter excuter){
	 this.context=context;
	 this.excuter=excuter;
 }
 public List<City> getFavoriteCity(){
	 List <City> cityList=excuter.getFavoireCity();
	 return cityList;
 }
 
 public void setFavority(City city,boolean isFavority){
	 excuter.setFavoriteCity(city.getNumber(), isFavority);
	 city.setFavorite(isFavority);
	 if(isFavority){
		 T.showShort(context, R.string.set_favorite_success);
	 }else{
		 T.showShort(context, R.string.set_unfavorite_success);
	 }
	 
 }
 
}
