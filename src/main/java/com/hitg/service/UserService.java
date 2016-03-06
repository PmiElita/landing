package com.hitg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitg.dao.CategoryDAO;
import com.hitg.dao.TripDataDAO;
import com.hitg.dao.UserDAO;
import com.hitg.domain.TripData;
import com.hitg.domain.User;

@Service("userService")
public class UserService {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private TripDataDAO tripDataDAO;
	
	public User findUserByChatId(Long chatId){
		return userDAO.findByChatId(chatId);
	}
	public TripData getTripData(User user){
		return tripDataDAO.findByUserAndIsActive(user, true);
	}
	public void addCategory(User user, String name) {
	 user.getCategory().add(categoryDAO.findByName(name));
	 userDAO.save(user);
		
	}
}
