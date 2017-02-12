package com.coolweather.app.model;

public class County {

	private int id;
	private String county_name;
	private String county_code;
	private int city_id;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getCountyName(){
		return county_name;
	}
	
	public void setCountyName(String county_name){
		this.county_name=county_name;
	}
	
	public String getCountyCode(){
		return county_code;
	}
	public void setCountyCode(String county_code){
		this.county_code=county_code;
	}
	
	public int getCityId(){
		return city_id;
	}
	
	public void setCityId(int city_id){
		this.city_id=city_id;
	}
	
}
