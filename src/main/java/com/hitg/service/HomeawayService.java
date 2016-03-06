package com.hitg.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hitg.dto.AuthTokenResponseDTO;
import com.hitg.dto.HomeawaySearchResult;
import com.hitg.util.Encoder;

@Service("homeaway")
public class HomeawayService {
	private static final String HOMEAWAY_AUTH_URL = "https://ws.homeaway.com/oauth/token";
	private static final String HOMEAWAY_SEARCH_URL = "https://ws.homeaway.com/public/search";
	@Value("${bot.homeaway.client_secret}")
	private String CLIENT_SECRET;
	
	@Value("${bot.homeaway.client_id}")
	private String CLIENT_ID;
	
	@Value("${bot.homeaway.pagesize}")
	private String PAGE_SIZE;
	
	private String token;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public HomeawaySearchResult search(String country, String city, Double maxPrice, LocalDate date, int duration, Integer minSleeps){

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("pageSize", PAGE_SIZE);
		params.add("availabilityStart", date.format(DateTimeFormatter.ISO_DATE));
		params.add("availabilityEnd", date.plusDays(duration).format(DateTimeFormatter.ISO_DATE));
		params.add("maxPrice", maxPrice.toString());
		params.add("q", country + "," + city);
		params.add("minSleeps", minSleeps.toString());
		params.add("sort", "prices:asc");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOMEAWAY_SEARCH_URL).queryParams(params);
 		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<HomeawaySearchResult> responseEntity = restTemplate.exchange(builder.toUriString(),HttpMethod.GET, entity,  HomeawaySearchResult.class);
		return responseEntity.getBody();
	}

	private String getToken() {
		if (token==null){
			String authToken = Encoder.encodeBase64(CLIENT_ID+":"+CLIENT_SECRET);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + authToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<AuthTokenResponseDTO> responseEntity = restTemplate.exchange(HOMEAWAY_AUTH_URL,HttpMethod.POST, entity, AuthTokenResponseDTO.class);
			token = responseEntity.getBody().getAccessToken();
		}
		return token;
	}
		
	
}
