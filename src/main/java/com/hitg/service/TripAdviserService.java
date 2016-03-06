package com.hitg.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hitg.domain.Category;
import com.hitg.dto.TripAdviserResultDTO;

@Service("tripAdviserService")
public class TripAdviserService {
	private static final String API_URL = "https://app.xapix.io/api/v1/ITravelBot/sites";
	@Autowired
	private RestTemplate restTemplate;

	private List<TripAdviserResultDTO> results;

	private List<TripAdviserResultDTO> getResults() {
		if (results == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/json");
			headers.set("Authorization-Token", "ZLm4dqJSNi08zb7GHV1WB3OQDaCF6rTe");
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<List<TripAdviserResultDTO>> resultEntity = restTemplate.exchange(API_URL, HttpMethod.GET,
					entity, new ParameterizedTypeReference<List<TripAdviserResultDTO>>() {
					});
			results = resultEntity.getBody();
		}
		return results;
	}
	
	public Map<String,List<TripAdviserResultDTO>> getAdvises(List<Category> categories){
		List<String> categoriesNames = categories.stream().map(Category::getName).collect(Collectors.toList());
		return getResults().stream().filter(advise-> categoriesNames.contains(advise.getType())||categoriesNames.contains(advise.getSubcategory())).sorted((first,second)->second.getRating().compareTo(first.getRating())).collect(Collectors.groupingBy(advise-> advise.getCityName(), Collectors.toList()));
	}
	
	

}
