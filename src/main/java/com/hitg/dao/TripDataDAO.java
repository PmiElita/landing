package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.TripData;
import com.hitg.domain.User;

public interface TripDataDAO extends JpaRepository<TripData, Integer> {
	TripData findByUserAndIsActive(User user, Boolean isActive);
}
