package com.hitg.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitg.dto.LocationDTO;

@Service("googleService")
public class GoogleService {
	@Autowired
	private RestTemplate restTemplate;
	
	public LocationDTO getLocation(String lat, String lon){
		String response = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(response).get("results").get(0).get("address_components");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LocationDTO result = new LocationDTO();
		for(JsonNode element :node ){
			if (element.get("types").get(0).asText().equals("locality")){
				result.setCity(element.get("long_name").asText());
			}
			if (element.get("types").get(0).asText().equals("country")){
				result.setCountry(element.get("long_name").asText());
			}
		}
		return result;
	}
}
