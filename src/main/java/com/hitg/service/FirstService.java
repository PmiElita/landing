package com.hitg.service;

import org.springframework.stereotype.Service;

@Service("firstService")
public class FirstService {
	public void sayHello(){
		System.out.println("Hello");
	}
}
