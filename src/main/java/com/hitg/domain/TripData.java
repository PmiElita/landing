package com.hitg.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hitg.jpa.converter.LocalDatePersistenceConverter;

@Entity
@Table(name ="trip_data")
public class TripData {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "start_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	private LocalDate startDate;
	
	@Column(name = "end_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	private LocalDate endDate;
	
	@Column(name = "budget")
	private Integer budget;
	
	@ManyToOne
	private User user;

	@Column(name = "is_active")
	private Boolean isActive;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}
	
	


}
