package com.hitg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TripAdviserResultDTO {
	@JsonProperty("s_id")
	private Integer id;
	
	@JsonProperty("activity_name")
	private String activityName;
	
	@JsonProperty("country_name")
	private String countryName;
	
	@JsonProperty("city_name")
	private String cityName;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("subcategory")
	private String subcategory;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("rating")
	private Double rating;
	
	@JsonProperty("image_url")
	private String imageUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "TripAdviserResultDTO [id=" + id + ", activityName=" + activityName + ", countryName=" + countryName
				+ ", cityName=" + cityName + ", type=" + type + ", state=" + state + ", subcategory=" + subcategory
				+ ", address=" + address + ", rating=" + rating + ", imageUrl=" + imageUrl + "]";
	}
	
	
}
