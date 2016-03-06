package com.hitg.service;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;

import com.hitg.dao.UserDAO;
import com.hitg.domain.User;

@Service("messageService")
public class MessageService {
	@Autowired
	private UserDAO userDAO;
	public String getStartMessage(Message message){
		User user = new User();
		user.setChatId(message.getChatId());
		user.setFirstName("Oleh");
		userDAO.save(user);
		
		return "Hello, " + user.getFirstName();
		
	}
}
