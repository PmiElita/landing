package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Country;

public interface CountryDAO extends JpaRepository<Country, Integer> {
	Country findByName(String name);
}
