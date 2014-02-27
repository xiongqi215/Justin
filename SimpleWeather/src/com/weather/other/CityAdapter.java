package com.weather.other;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.simple.weather.R;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;
import com.weather.view.PinnedHeaderListView;
import com.weather.view.PinnedHeaderListView.PinnedHeaderAdapter;
import com.wheather.util.T;

public class CityAdapter extends BaseAdapter implements PinnedHeaderAdapter, SectionIndexer,OnScrollListener{
	private List<City> cities;
	private LayoutInflater inflater;
	private List<Integer> positions;// 首字母位置集
	private List<String> sections;
	private Map<String, List<City>> pinyinMap;
	private FavoriteCityHandler favoriteCityHandler;
	public CityAdapter(Context context,CityList cityList) {
		inflater = LayoutInflater.from(context);
		cities = cityList.getCityList();
		positions = cityList.getPositions();
		sections = cityList.getSections();
		pinyinMap = cityList.getPinyinMap();
	}

	public void setFavoriteCityHandler(FavoriteCityHandler favoriteCityHandler) {
		this.favoriteCityHandler = favoriteCityHandler;
	}

	@Override
	public int getCount() {
		return this.cities.size();
	}

	@Override
	public City getItem(int position) {
		int section = getSectionForPosition(position);
		return pinyinMap.get(sections.get(section)).get(
				position - getPositionForSection(section));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.city_list_item, null);
		TextView city = (TextView) convertView.findViewById(R.id.city_title);
		TextView group = (TextView) convertView.findViewById(R.id.group_title);
		ImageView favorite=(ImageView) convertView.findViewById(R.id.btn_sc);
		int section = this.getSectionForPosition(position);// 计算当前是哪个区域
		int fristIndex = getPositionForSection(section);
		;// 计算当前区域起始元素索引
		if (fristIndex == position) {// 判断前期位置是否为当前区域的起始位置
			group.setVisibility(View.VISIBLE);
			group.setText(sections.get(section));
		} else {
			group.setVisibility(View.GONE);
		}
		final City item = pinyinMap.get(sections.get(section)).get(
				position - fristIndex);
		city.setText(item.getCity());
		if(item.isFavorite()){
			favorite.setImageResource(R.drawable.sc);
		}
		favorite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(favoriteCityHandler!=null){
					boolean isFavority=(item.isFavorite())?false:true;
					favoriteCityHandler.setFavority(item, isFavority);
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
	public int getPositionForSection(int section) {// 从首字母位置集中获取当前字母域的的索引

		if (section < 0 || section >= positions.size()) {
			return -1;
		}
		return positions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {// 从首字母位置集中获取当前view位置是否是一个字母域的开始，若果是返回位置索引
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(positions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

	@Override
	public Object[] getSections() {
		return sections.toArray();
	}
	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || position >= getCount()) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.group_title)).setText(title);
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}
}
