package com.hitg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitg.domain.City;

public class CityCodeResponse {
	@JsonProperty("Places")
	private List<City> places;

	public List<City> getPlaces() {
		return places;
	}

	public void setPlaces(List<City> places) {
		this.places = places;
	}
}
