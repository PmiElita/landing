package com.hitg.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.ReplyKeyboardMarkup;

import com.hitg.dao.AnswerDAO;
import com.hitg.dao.AskedQuestionDAO;
import com.hitg.dao.CategoryDAO;
import com.hitg.dao.QuestionDAO;
import com.hitg.dao.QuestionOrderDAO;
import com.hitg.dao.TripDataDAO;
import com.hitg.dao.UserDAO;
import com.hitg.domain.Answer;
import com.hitg.domain.AnswerType;
import com.hitg.domain.AskedQuestion;
import com.hitg.domain.Category;
import com.hitg.domain.Question;
import com.hitg.domain.QuestionOrder;
import com.hitg.domain.TripData;
import com.hitg.domain.User;
import com.hitg.domain.UserState;
import com.hitg.dto.LocationDTO;

@Service("messageService")
public class MessageService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private QuestionDAO questionDAO;
	@Autowired
	private AskedQuestionDAO askedQuestionDAO;
	@Autowired
	private AnswerDAO answeDAO;
	@Autowired
	private QuestionOrderDAO questionOrderDAO;
	@Autowired
	private TripDataDAO tripDataDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private TripAdviserService tripAdviserService;
	@Autowired
	private GoogleService googleService;

	public SendMessage getStartMessage(Message message) {
		User user = new User();
		SendMessage sendMessage = new SendMessage();
		user.setChatId(message.getChatId());
		user.setFirstName(message.getFrom().getFirstName());
		user.setLastName(message.getFrom().getLastName());
		user.setUsername(message.getFrom().getUserName());
		user.setState(UserState.NEW);
		userDAO.save(user);
		Question initialQuestion = questionDAO.findOne(1);
		AskedQuestion askedQuestion = new AskedQuestion();
		askedQuestion.setQuestion(initialQuestion);
		askedQuestion.setIsActive(true);
		askedQuestion.setUser(user);
		askedQuestionDAO.save(askedQuestion);
		sendMessage.setText(initialQuestion.getBody());
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<List<String>> keyboard = new ArrayList<>();
		List<String> buttons = questionOrderDAO.findByParentQuestion(initialQuestion).stream()
				.map(questionOrder -> questionOrder.getAnswer().getBody()).collect(Collectors.toList());
		keyboard.add(buttons);

		replyKeyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplayMarkup(replyKeyboardMarkup);
		return sendMessage;

	}

	public SendMessage getNextMessage(User user, Message message) {
		AskedQuestion askedQuestion = askedQuestionDAO.findLastByUser(user.getId());
		Question question = askedQuestion.getQuestion();
		
		SendMessage sendMessage = new SendMessage();
		if (question.getType() == AnswerType.LOCATION){
			
			Location location = message.getLocation();
			
			LocationDTO loc = new LocationDTO();
			user.setCity(loc.getCity());
			user.setCountry(loc.getCountry());
			userDAO.save(user);
			AskedQuestion nextAskedQuestion = new AskedQuestion();
			Question nextQuestion = questionDAO.findOne(2);
			nextAskedQuestion.setQuestion(nextQuestion);
			nextAskedQuestion.setIsActive(true);
			nextAskedQuestion.setUser(user);
			askedQuestionDAO.save(nextAskedQuestion);
			sendMessage.setText(nextQuestion.getBody());
			return sendMessage;
			
		}
		if (question.getType()==AnswerType.USER_INPUT){
			sendMessage.setText(getMessageBody(message, question,sendMessage));
			return sendMessage;
		} 
		
		if (question.getId().equals(6)){
			Category category = categoryDAO.findByName(message.getText().trim());
			if (category.getId().equals(1)||category.getId().equals(2)){
				sendMessage.setText("Sweet! Do you want to become more specific? ");
				List<String>  buttons = categoryDAO.findByCategory(category).stream().map(Category::getName).collect(Collectors.toList());
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				List<List<String>> keyboard = new ArrayList<>();
				keyboard.add(buttons);
				replyKeyboardMarkup.setKeyboard(keyboard);
				sendMessage.setReplayMarkup(replyKeyboardMarkup);
				return sendMessage;
			}
			user.getCategory().add(category);
			userDAO.save(user);
		}
		Answer answer = answeDAO.findByBody(message.getText());
	
		if (answer==null){
		sendMessage.setText("Sorry, I didn't get it");
		return sendMessage;
		}
		QuestionOrder order = questionOrderDAO.findByParentQuestionAndAnswer(question, answer);
		Question nextQuestion = order.getChildQuestion();
		sendMessage.setText(nextQuestion.getBody());
		if (nextQuestion.getType() == AnswerType.BUTTONS) {
			 
				
				
			
				List<String>  buttons = questionOrderDAO.findByParentQuestion(nextQuestion).stream()
					.map(questionOrder -> questionOrder.getAnswer().getBody()).collect(Collectors.toList());
		
			ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
			List<List<String>> keyboard = new ArrayList<>();
			keyboard.add(buttons.subList(0, 2));
			keyboard.add(buttons.subList(2, 4));
			replyKeyboardMarkup.setKeyboard(keyboard);
			sendMessage.setReplayMarkup(replyKeyboardMarkup);
			
		}
		AskedQuestion nextAskedQuestion = new AskedQuestion();
		nextAskedQuestion.setQuestion(nextQuestion);
		nextAskedQuestion.setIsActive(true);
		nextAskedQuestion.setUser(user);
		askedQuestionDAO.save(nextAskedQuestion);
		return sendMessage;
	}

	private String getMessageBody(Message message, Question question, SendMessage sendMessage) {
		User user = userDAO.findByChatId(message.getChatId());
		TripData tripData = tripDataDAO.findByUserAndIsActive(user, true);
		if (tripData==null){
			tripData = new TripData();
			tripData.setUser(user);
			tripData.setIsActive(true);
		}
		AskedQuestion nextAskedQuestion = new AskedQuestion();
		if (question.getId().equals(2)){
			try{
				tripData.setStartDate(LocalDate.parse(message.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				Question nextQuestion = questionDAO.findOne(4);
				
				nextAskedQuestion.setQuestion(nextQuestion);
				nextAskedQuestion.setIsActive(true);
				nextAskedQuestion.setUser(user);
				askedQuestionDAO.save(nextAskedQuestion);
			} catch(DateTimeParseException e){
				return "Sorry, i didn't get that, could you please repeat. (dd/mm/yyyy)";
			}
		} else if (question.getId().equals(4)){
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(message.getText());
			if(m.find()){
				tripData.setEndDate(tripData.getStartDate().plusDays(Integer.parseInt(m.group())));
			} else {
				return "Sorry, can't get it, please provide a digit"; 
			}
			Question nextQuestion = questionDAO.findOne(5);
			nextAskedQuestion.setQuestion(nextQuestion);
			nextAskedQuestion.setIsActive(true);
			nextAskedQuestion.setUser(user);
			askedQuestionDAO.save(nextAskedQuestion);
		} else if (question.getId().equals(5)){
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(message.getText());
			if(m.find()){
				tripData.setBudget(Integer.parseInt(m.group()));
			} else {
				return "Sorry, can't get it, please provide a digit"; 
			}
			Question nextQuestion = questionDAO.findOne(6);
			nextAskedQuestion.setQuestion(nextQuestion);
			nextAskedQuestion.setIsActive(true);
			nextAskedQuestion.setUser(user);
			askedQuestionDAO.save(nextAskedQuestion);

			List<String>  buttons = categoryDAO.findByCategoryIsNull().stream().map(Category::getName).collect(Collectors.toList());
	
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<List<String>> keyboard = new ArrayList<>();
		keyboard.add(buttons);
		replyKeyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplayMarkup(replyKeyboardMarkup);
		}
		tripDataDAO.save(tripData);
		return nextAskedQuestion.getQuestion().getBody();
	}
	
}
