package com.hitg.dto;

public class PriceRange {
	private String currencyUnits;
	private Double from;
	private Double to;
	private String periodType;
	
	public String getCurrencyUnits() {
		return currencyUnits;
	}
	public void setCurrencyUnits(String currencyUnits) {
		this.currencyUnits = currencyUnits;
	}
	public Double getFrom() {
		return from;
	}
	public void setFrom(Double from) {
		this.from = from;
	}
	public Double getTo() {
		return to;
	}
	public void setTo(Double to) {
		this.to = to;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	@Override
	public String toString() {
		return "PriceRange [currencyUnits=" + currencyUnits + ", from=" + from + ", to=" + to + ", periodType="
				+ periodType + "]";
	}
	
	
}
