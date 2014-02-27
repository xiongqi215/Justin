package com.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.simple.weather.R;
import com.weather.other.CityAdapter;
import com.weather.other.CityList;
import com.weather.other.FavoriteCityAdpater;
import com.weather.other.FavoriteCityHandler;
import com.weather.other.SearchAdapter;
import com.weather.util.db.City;
import com.weather.util.db.CityDBExcuter;
import com.weather.view.BladeView;
import com.weather.view.PinnedHeaderListView;

public class CitySelectActivity extends Activity implements TextWatcher,OnClickListener{
	private CityAdapter cityListadapter;
	private CityList cityList;
	private PinnedHeaderListView cityListView;
	private ImageView back;
	private BladeView letterBar;
	private ImageButton btn_clear;
	private EditText search_box;
	private ListView search_Result_List;
    private View search_List_Continer;
    private View city_List_Continer;
    private SearchAdapter searchAdapter;
	private CityDBExcuter excuter ;
    private FavoriteCityHandler favoriteCityHandler;
    private GridView favorite_view;
    private FavoriteCityAdpater favoriteCityAdpater;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case R.key.city_list_finish:
				initData();
				letterBar.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back_to_weather:
			backToWeather();
			break;
		case R.id.cancel_input:
			String text=search_box.getText().toString();
			if(!TextUtils.isEmpty(text)){
				search_box.setText("");
			}
			break;
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		this.setContentView(R.layout.city_selector);
		
		excuter=new CityDBExcuter(this);
		cityList = new CityList(getApplicationContext(), mHandler,excuter);

		cityListView = (PinnedHeaderListView) findViewById(R.id.City_List);
		cityListView.setEmptyView(findViewById(R.id.City_load_ProgressBar));
		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				City city = cityListadapter.getItem(position);
				refreshWeatherBySelected(city);

			}
		});

		back = (ImageView) findViewById(R.id.btn_back_to_weather);
		back.setOnClickListener(this);

		letterBar = (BladeView) this.findViewById(R.id.citys_letter_bar);
		letterBar.setOnItemClickListener(new BladeView.OnItemClickListener() {
			@Override
			public void onItemClick(String s) {
				if (cityList.getIndexer().get(s) != null) {
					cityListView.setSelection(cityList.getIndexer().get(s));
				}
			}
		});

		letterBar.setVisibility(View.GONE);
		cityListView.setVisibility(View.GONE);
		search_box=(EditText) this.findViewById(R.id.city_name_inputer);
		search_box.addTextChangedListener(this);
		btn_clear=(ImageButton) this.findViewById(R.id.cancel_input);
		btn_clear.setOnClickListener(this);
		
		search_Result_List=(ListView) this.findViewById(R.id.Search_Result_List);
		search_Result_List.setEmptyView(this.findViewById(R.id.Search_Empty_Msg));
		search_Result_List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				City city = searchAdapter.getItem(position);
				refreshWeatherBySelected(city);

			}
		});
		city_List_Continer=this.findViewById(R.id.City_List_Continer);
		city_List_Continer.setVisibility(View.VISIBLE);
		
		search_List_Continer=this.findViewById(R.id.Search_List_Continer);
		search_List_Continer.setVisibility(View.GONE);
		
//		favorite_view=(GridView) this.findViewById(R.id.favorite_view);
//		favoriteCityAdpater=new FavoriteCityAdpater(this, excuter.getFavoireCity());
//		favorite_view.setAdapter(favoriteCityAdpater);

		
	}

	public void backToWeather() {
		this.finish();
	}

	public void initData() {
		favoriteCityHandler=new FavoriteCityHandler(this, excuter);
		cityListadapter = new CityAdapter(CitySelectActivity.this, cityList);
		cityListadapter.setFavoriteCityHandler(favoriteCityHandler);
		cityListView.setAdapter(cityListadapter);
		cityListView.setOnScrollListener(cityListadapter);
		cityListView.setPinnedHeaderView(LayoutInflater.from(
				CitySelectActivity.this).inflate(R.layout.city_list_item_title,
				cityListView, false));
		
		searchAdapter=new SearchAdapter(getApplicationContext(), cityList.getCityList());
		searchAdapter.setFavoriteCityHandler(favoriteCityHandler);
		this.search_Result_List.setAdapter(searchAdapter);
		this.search_Result_List.setTextFilterEnabled(true);
	}

	public void refreshWeatherBySelected(City city) {
		Intent intent = new Intent();
		intent.setClass(CitySelectActivity.this, WeatherActivity.class);
		intent.putExtra("City", city);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if(TextUtils.isEmpty(s)){
			this.city_List_Continer.setVisibility(View.VISIBLE);
			this.search_List_Continer.setVisibility(View.GONE);
			this.btn_clear.setVisibility(View.GONE);
		}else{
			this.city_List_Continer.setVisibility(View.GONE);
			this.search_List_Continer.setVisibility(View.VISIBLE);
			this.btn_clear.setVisibility(View.VISIBLE);
			searchAdapter.getFilter().filter(s);
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		this.excuter.close();
	}

	

}
