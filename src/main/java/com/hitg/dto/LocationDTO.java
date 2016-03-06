package com.hitg.dto;

public class LocationDTO {
	private String country;
	private String city;
	public String getCountry() {
		return "Germany";
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return "Berlin";
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "LocationDTO [country=" + country + ", city=" + city + "]";
	}
}
