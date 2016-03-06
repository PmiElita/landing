package com.hitg.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City {
	@JsonProperty("PlaceId")
	private String placeId;
	
	@JsonProperty("PlaceName")
	private String placeName;
	
	@JsonProperty("CountryName")
	private String countryName;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	@Override
	public String toString() {
		return "City [placeId=" + placeId + ", placeName=" + placeName + ", countryName=" + countryName + "]";
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	
}
