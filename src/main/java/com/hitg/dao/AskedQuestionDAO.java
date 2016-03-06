package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitg.domain.AskedQuestion;
import com.hitg.domain.User;

public interface AskedQuestionDAO extends JpaRepository<AskedQuestion, Integer> {
	@Query(value = "Select aq.* FROM asked_question as aq WHERE aq.id = (SELECT max(aqMax.id) FROM asked_question as aqMax WHERE aqMax.user_id=:user_id)", nativeQuery = true)
	AskedQuestion findLastByUser(@Param("user_id") Integer userId);
}
