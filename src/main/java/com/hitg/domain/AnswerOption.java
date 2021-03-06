package com.hitg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="answer_option")
public class AnswerOption {

		@Id
		@GeneratedValue
		@Column(name="id")
		private Integer id;
		
		@Column(name="value")
		private String value;
		
		@ManyToOne
		private Answer answer;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Answer getAnswer() {
			return answer;
		}

		public void setAnswer(Answer answer) {
			this.answer = answer;
		}
		
		
}
