package com.hitg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.Answer;
import com.hitg.domain.Question;
import com.hitg.domain.QuestionOrder;

public interface QuestionOrderDAO extends JpaRepository<QuestionOrder, Integer> {
	QuestionOrder findByParentQuestionAndAnswer(Question parent, Answer answer);
	
	List<QuestionOrder> findByParentQuestion(Question parent);
}
