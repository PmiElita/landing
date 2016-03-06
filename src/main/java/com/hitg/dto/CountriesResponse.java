package com.hitg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitg.domain.Country;

public class CountriesResponse {
	@JsonProperty("Countries")
	private List<Country> countries;

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	
	
}
