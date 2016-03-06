package com.hitg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "question_order")
public class QuestionOrder {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name ="parent_id")
	private Question parentQuestion;
	
	@ManyToOne
	@JoinColumn(name ="child_id")
	private Question childQuestion;
	
	@ManyToOne
	@JoinColumn(name ="answer_id")
	private Answer answer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Question getParentQuestion() {
		return parentQuestion;
	}

	public void setParentQuestion(Question parentQuestion) {
		this.parentQuestion = parentQuestion;
	}

	public Question getChildQuestion() {
		return childQuestion;
	}

	public void setChildQuestion(Question childQuestion) {
		this.childQuestion = childQuestion;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

}
