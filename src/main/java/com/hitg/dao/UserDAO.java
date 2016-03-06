package com.hitg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitg.domain.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	User findByChatId(Long chatId);
}
