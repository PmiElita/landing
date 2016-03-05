package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Question;

public interface QuestionDAO extends JpaRepository<Question, Integer> {

}
