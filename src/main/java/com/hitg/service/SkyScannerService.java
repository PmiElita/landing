package com.hitg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hitg.dao.CountryDAO;
import com.hitg.dto.CountriesResponse;

@Service("skyScannerService")
public class SkyScannerService {
	private static final String LOCALE = "";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CountryDAO countryDAO;
	
	public void getCountries(){
		CountriesResponse response = restTemplate.getForObject("http://partners.api.skyscanner.net/apiservices/reference/v1.0/countries/en-GB?apiKey=prtl6749387986743898559646983194", CountriesResponse.class);
		response.getCountries().forEach(country->countryDAO.save(country));
	}
	

}
