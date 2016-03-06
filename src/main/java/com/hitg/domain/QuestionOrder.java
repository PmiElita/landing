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

}
