package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {


	//ָ�����ݿ������
	public static final String DB_NAME="cool_weather";
	//ָ�����ݿ�İ汾
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	//�����췽��˽�л����������ݿ⣬��ȡ���ݿ����
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,
				DB_NAME,null, VERSION);
		db=dbHelper.getWritableDatabase();		
	}

	//��ȡCoolWeatherDBʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;		
	}
	
	//��ʡ�ݴ��浽���ݿ�������
	public void saveProvince(Province province){
		if(province!=null){
		ContentValues values=new ContentValues();
		values.put("id", province.getId());
		values.put("province_name", province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
		}
	}
		
	//��ȡ���ݿ��е�ʡ����Ϣ
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getColumnIndex("id"));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			    list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;		
	}
	
	//���д��浽���ݿ���
	public void saveCity(City city){
		if(city!=null){
		ContentValues values=new ContentValues();
		values.put("id", city.getId());
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
		}
					
	}
		
	//��ȡ���ݿ��е��е���Ϣ
	public List<City> loadCities(int province_id){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null,"province_id=?",new String[]{String.valueOf(province_id)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getColumnIndex("id"));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getColumnIndex("province_id"));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	//���ش��浽���ݿ���
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values=new ContentValues();
			values.put("id", county.getId());
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	//��ȡ���ݿ����ص���Ϣ
	
	public List<County> loadCounties(int city_id){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?",
				new String[]{String.valueOf(city_id)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getColumnIndex("id"));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getColumnIndex("city_id"));		
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();		
		}
		
		return list;
		
	}
	
}
