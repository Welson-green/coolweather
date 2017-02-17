package com.coolweather.app;

import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private TextView cityNameText;
	private TextView publishTimeText;
	private TextView currentDateText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private Button switch_city;
	private Button refresh_weather;
	private LinearLayout weatherInfoLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishTimeText=(TextView)findViewById(R.id.publish_text);
		currentDateText=(TextView)findViewById(R.id.current_date);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		switch_city=(Button)findViewById(R.id.switch_city);
		refresh_weather=(Button)findViewById(R.id.refresh_weather);
		switch_city.setOnClickListener(this);
		refresh_weather.setOnClickListener(this);
		
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//���ؼ����žͲ�ѯ����
			publishTimeText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
	}

	//��ѯ�ؼ���������Ӧ����������
	private void queryWeatherCode(String countyCode){
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}

	//��ѯ������������Ӧ������
	private void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	
	//���ݴ���ĵ�ַ���������������ѯ�������Ż���������������Ӧ����Ϣ
	private void queryFromServer(String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
					String[] array=response.split("\\|");//ע��˴��ķ��ţ�б�ܵķ����ܴ�
					if(array!=null && array.length==2){
					String weatherCode=array[1];
					queryWeatherInfo(weatherCode);
					}
					}
				}else if("weatherCode".equals(type)){
					//������������ص�������Ϣ
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();//��ʾ��������
						}
						
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishTimeText.setText("ͬ��ʧ��");
					}
					
				});
			}
			
		});
	}
	
	//��SharedPreferences�ļ���ȡ�������������Ϣ����������Ϣ��ʾ��������
	public void showWeather(){
		SharedPreferences pref=
				PreferenceManager.getDefaultSharedPreferences(this);
		
		cityNameText.setText(pref.getString("city_name", ""));
		publishTimeText.setText("����"+pref.getString("publish_time", "")+"����");
		currentDateText.setText(pref.getString("current_date", ""));
		weatherDespText.setText(pref.getString("weather_desp", ""));
		temp1Text.setText(pref.getString("temp1", ""));
		temp2Text.setText(pref.getString("temp2", ""));
	    weatherInfoLayout.setVisibility(View.VISIBLE);
	    cityNameText.setVisibility(View.VISIBLE);
	    
	    Intent intent=new Intent(WeatherActivity.this,AutoUpdateService.class);
	    startService(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent=new Intent(WeatherActivity.this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);			
			finish();
			break;
			
		case R.id.refresh_weather:
			publishTimeText.setText("ͬ����");
			SharedPreferences pref=
			    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
			String weatherCode=pref.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
			queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		
		}
	}

	

	

	
	
}
