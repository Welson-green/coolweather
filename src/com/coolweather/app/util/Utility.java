package com.coolweather.app.util;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.text.TextUtils;

public class Utility {
	//�������������ص�ʡ����Ϣ
	public synchronized static boolean handleProvincesResponse(String response,CoolWeatherDB coolWeatherDB){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
			for(String p:allProvinces){
				String[] array=p.split("\\|");
				Province province=new Province();
				province.setProvinceCode(array[0]);
				province.setProvinceName(array[1]);
				coolWeatherDB.saveProvince(province);
			}
			return true;
			}
		}
		
		return false;
		
	}
	
	
	//�������������صĵ��е���Ϣ
	public  static boolean handleCityResponse(String response,
			CoolWeatherDB coolWeatherDB,int province_id){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(province_id);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}		
		return false;
		
	}
	
	//���������ص���Ϣ
	public static boolean handleCountyResponse(String response,
			CoolWeatherDB coolWeatherDB,int city_id){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
			for(String c:allCounties){
				String[] array=c.split("\\|");
				County county=new County();
				county.setCountyCode(array[0]);
				county.setCountyName(array[1]);
				county.setCityId(city_id);
				coolWeatherDB.saveCounty(county);
				
			}
			return true;
			}
			
		}
		
		return false;
		
	}
	

}
