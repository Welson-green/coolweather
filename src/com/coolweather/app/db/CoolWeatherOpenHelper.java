package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

	//Province建表语句
	private static final String CREATE_PROVINCE="create table Province(" +
			"id integer primary key autoincrement," +
			"province_name text," +
			"province_code text)";
	
	//city建表语句
	private static final String CREATE_CITY="create table City(" +
			"id integer primary key autoincrement," +
			"city_name text," +
			"city_code," +
			"province_id integer)";
	
	//county建表语句
	private static final String CREATE_COUNTY="create table County(" +
			"id integer primary key autoincrement," +
			"county_name," +
			"county_code," +
			"city_id integer)";
	
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);//创建province表
		db.execSQL(CREATE_CITY);//创建city表
		db.execSQL(CREATE_COUNTY);//创建county表
	}


	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

}
