package com.hitg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeawayEntry {
	private String listingId;
	private String headline;
	private String listingUrl;
	private List<PriceRange> priceRanges;
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public List<PriceRange> getPriceRanges() {
		return priceRanges;
	}
	public void setPriceRanges(List<PriceRange> priceRanges) {
		this.priceRanges = priceRanges;
	}
	public String getListingUrl() {
		return listingUrl;
	}
	public void setListingUrl(String listingUrl) {
		this.listingUrl = listingUrl;
	}
	@Override
	public String toString() {
		return "HomeawayEntry [listingId=" + listingId + ", headline=" + headline + ", priceRanges=" + priceRanges
				+ "]";
	}
	
	
}
