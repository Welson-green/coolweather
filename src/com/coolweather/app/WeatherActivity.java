package com.coolweather.app;

import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{

	private TextView cityNameText;
	private TextView publishTimeText;
	private TextView currentDateText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	
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
	public void queryWeatherCode(String countyCode){
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}

	//��ѯ������������Ӧ������
	public void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	
	//���ݴ���ĵ�ַ���������������ѯ�������Ż���������������Ӧ����Ϣ
	public void queryFromServer(String address,final String type){
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
	}
	
	
}
