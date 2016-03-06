package com.hitg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown= true)
public class QuotesResponseDTO {
	@JsonProperty("data")
	private List<Quote> data;

	public List<Quote> getData() {
		return data;
	}

	public void setData(List<Quote> data) {
		this.data = data;
	}
}
