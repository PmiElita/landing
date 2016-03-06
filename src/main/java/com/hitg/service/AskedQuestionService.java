package com.hitg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitg.dao.AskedQuestionDAO;
import com.hitg.dao.UserDAO;
import com.hitg.domain.AskedQuestion;
import com.hitg.domain.User;

@Service("askedQuestion")
public class AskedQuestionService {
 @Autowired
 private AskedQuestionDAO askedQuestionDAO;
 
 public AskedQuestion getLastAskedQuestion(User user){
 
  return askedQuestionDAO.findLastByUser(user.getId());
 }
 

}