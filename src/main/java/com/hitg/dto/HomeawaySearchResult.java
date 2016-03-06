package com.hitg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class HomeawaySearchResult {
	private List<HomeawayEntry> entries;

	public List<HomeawayEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<HomeawayEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "HomeawaySearchResult [entries=" + entries + "]";
	}
	
	
}
