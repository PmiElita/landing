package com.hitg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("tripAdviserService")
public class TripAdviserService {
	
	@Autowired
	private RestTemplate restTemplate;
	
}
