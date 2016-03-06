package com.hitg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Category;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
	List<Category> findByCategoryIsNull();
	List<Category> findByCategory(Category category);
	Category findByName(String name);
}
