package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Category;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

}
