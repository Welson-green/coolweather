package com.coolweather.app;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{

	private static final int PROVINCE_LEVEL=0;
	private static final int CITY_LEVEL=1;
	private static final int COUNTY_LEVEL=2;
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;
	
	
	
	
	private  int CURRENT_LEVEL;
	private ProgressDialog progressDialog;
	private ListView listView;
	private TextView title_text;
	private ArrayAdapter<String> adapter;
	private List<String> dataList=new ArrayList<String>();
	private CoolWeatherDB coolWeatherDB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
	    title_text=(TextView)findViewById(R.id.title_text);
	    adapter=new ArrayAdapter<String>(this,
	    	android.R.layout.simple_list_item_1,dataList);
	    listView.setAdapter(adapter);
	    coolWeatherDB=CoolWeatherDB.getInstance(this);
	    listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if(CURRENT_LEVEL==PROVINCE_LEVEL){
					selectedProvince=provinceList.get(index);
					queryCities();
				}else if(CURRENT_LEVEL==CITY_LEVEL){
					selectedCity=cityList.get(index);
					queryCounties();
				}
			}
	    	
	    });
	    queryProvinces();
	}	
	
	private void queryProvinces(){
		provinceList=coolWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province p:provinceList){
				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			title_text.setText("中国");
			CURRENT_LEVEL=PROVINCE_LEVEL;
		}else{
			queryFromServer(null,"province");
		}
	}
	
	private void queryCities(){
		cityList=coolWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();//清除之前的数据
			for(City c:cityList){
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			title_text.setText(selectedProvince.getProvinceName());
			CURRENT_LEVEL=CITY_LEVEL;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	
	private void queryCounties(){
		countyList=coolWeatherDB.loadCounties(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County c:countyList){
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			title_text.setText(selectedCity.getCityName());
			CURRENT_LEVEL=COUNTY_LEVEL;
		}else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	
	private void queryFromServer( final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvincesResponse(response, coolWeatherDB);
				}else if("city".equals(type)){
					result=Utility.handleCityResponse(response, coolWeatherDB, selectedProvince.getId());
				}else if("county".equals(type)){
					result=Utility.handleCountyResponse(response, coolWeatherDB, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
						
					});
				}
			}

			@Override
			public void onError(Exception Exception) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "Fail to load!!!", Toast.LENGTH_SHORT).show();
					}					
				});
			}
			
		});
	}
	
	//显示进度条对话框
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("loading......");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	
	//关闭进度条对话框
	private void closeProgressDialog(){
		if(progressDialog!=null)
		{
			progressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(CURRENT_LEVEL==COUNTY_LEVEL){
			queryCities();
		}else if(CURRENT_LEVEL==CITY_LEVEL){
			queryProvinces();
		}else{
			finish();
		}
	}
	
}
