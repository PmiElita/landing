package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Answer;

public interface AnswerDAO extends JpaRepository<Answer, Integer> {
	Answer findByBody(String body);
}
