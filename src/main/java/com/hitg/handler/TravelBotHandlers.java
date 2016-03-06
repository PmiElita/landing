package com.hitg.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.methods.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.hitg.BotConfig;
import com.hitg.BotEngineApplication;
import com.hitg.domain.TripData;
import com.hitg.domain.User;
import com.hitg.dto.HomeawaySearchResult;
import com.hitg.dto.TripAdviserResultDTO;
import com.hitg.service.AskedQuestionService;
import com.hitg.service.HomeawayService;
import com.hitg.service.MessageService;
import com.hitg.service.PhotoService;
import com.hitg.service.SkyScannerService;
import com.hitg.service.TripAdviserService;
import com.hitg.service.UserService;

public class TravelBotHandlers extends TelegramLongPollingBot {

	private static final List<String> CATEGORIES = Arrays.asList("nightlife", "shopping", "landmarks",
			"sightseeing tours", "museums", "adventure");
	private HomeawayService homeawayService;
	private MessageService messageService;

	private UserService userService;

	private TripAdviserService tripAdviserService;
	
	private SkyScannerService scannerService;

	private AskedQuestionService askedQuestionService;
	

	public TravelBotHandlers() {
		messageService = BotEngineApplication.CONTEXT.getBean(MessageService.class);
		userService = BotEngineApplication.CONTEXT.getBean(UserService.class);
		askedQuestionService = BotEngineApplication.CONTEXT.getBean(AskedQuestionService.class);
		tripAdviserService = BotEngineApplication.CONTEXT.getBean(TripAdviserService.class);
		homeawayService = BotEngineApplication.CONTEXT.getBean(HomeawayService.class);
		askedQuestionService = BotEngineApplication.CONTEXT.getBean(AskedQuestionService.class);

		scannerService = BotEngineApplication.CONTEXT.getBean(SkyScannerService.class);

	}

	@Override
	public String getBotUsername() {

		return BotConfig.USERNAME;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			if (message.hasText() || message.hasLocation()) {
				try {
					try {
						handleIncomingMessage(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public String getBotToken() {

		return BotConfig.TOKEN;
	}

	private void handleIncomingMessage(Message message) throws TelegramApiException, IOException {
		SendMessage sendMessage = new SendMessage();
		SendPhoto sendPhoto = new SendPhoto();
		String chatId = message.getChatId().toString();
		PhotoService photoService = new PhotoService();
		int userId = message.getFrom().getId();

		User user = userService.findUserByChatId(message.getChatId());
		// if we have a new user
		if (user == null) {
			sendMessage = messageService.getStartMessage(message);
		} else {

			if (CATEGORIES.contains(message.getText().trim())) {
				sendPhoto(user, message);
			}
			sendMessage = messageService.getNextMessage(user, message);

			// QuaskedQuestion.getQuestion();
		}

		sendMessage.setChatId(chatId);
		sendMessage(sendMessage);

		// String imageUrl=
		// "https://learninglivingfreely.files.wordpress.com/2014/03/tumblr_n320ytza511rmyav1o1_500.jpg";
		// String imageUrl =
		// "http://cdn.trustedpartner.com/images/library/PalmBeachChamber2012/HomepageMedia/ADE12432-15C5-F1C2-6AEB2ECCE85E3D02/beach.png";
		// String savedImageName = photoService.savePhoto(imageUrl);
		// sendMessage.setChatId(chatId);
		// sendPhoto.setCaption("Adventure.jpg");
		// sendPhoto.setNewPhoto(savedImageName, "Adventure.jpg");
		// sendPhoto.setChatId(chatId);
		// sendPhoto(sendPhoto);
		//
		// String userFullName= message.getFrom().getFirstName() + " " +
		// message.getFrom().getLastName();
		// sendMessage.setText("How do you like it? " +userFullName );
		// ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		// List<List<String>> keyboarList = new ArrayList<List<String>>();
		// List<String> innerList = new ArrayList<String>();
		// innerList.add("1");
		// innerList.add("2");
		// innerList.add("3");
		// innerList.add("4");
		// List<String> innerList2 = new ArrayList<String>();
		// innerList2.add("5");
		// innerList2.add("6");
		// innerList2.add("7");
		// innerList2.add("8");
		// keyboarList.add(innerList);
		// keyboarList.add(innerList2);
		// replyKeyboardMarkup.setKeyboard(keyboarList);
		// sendMessage.setReplayMarkup(replyKeyboardMarkup);
		// sendMessage(sendMessage);
		// System.out.println(message.getText());

		// sendMessage(sendMessage);
	}

	private void sendPhoto(User user, Message message) {
		
		userService.addCategory(user,message.getText().trim() );
		PhotoService photoService = new PhotoService();
		TripData tripData = userService.getTripData(user);
		Map<String, List<TripAdviserResultDTO>> advises = tripAdviserService.getAdvises(user.getCategory());
		TripAdviserResultDTO advise = advises.get(advises.keySet().iterator().next()).get(0);
		HomeawaySearchResult result = homeawayService.search(advise.getCountryName(), advise.getCityName(), tripData.getBudget(), tripData.getStartDate()	,tripData.getEndDate(), 1);
		Double homeawayPrice = result.getEntries().get(0).getPriceRanges().get(0).getFrom()*(tripData.getEndDate().getDayOfYear()-tripData.getStartDate().getDayOfYear());
		Double scanenrPrice = scannerService.getMinQuotesPrice(user.getCountry(), user.getCity(),advise.getCountryName(), advise.getCityName(), tripData.getStartDate(),tripData.getEndDate());
		
		SendPhoto sendPhoto = new SendPhoto();
		
		String savedImageName = photoService.savePhoto(advise.getImageUrl());
		sendPhoto.setCaption("Adventure.jpg");
		sendPhoto.setNewPhoto(savedImageName, "Adventure.jpg");
		sendPhoto.setChatId(message.getChatId().toString());
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText(advise.getCountryName() + " " + advise.getCityName() + " " + (homeawayPrice.intValue()+scanenrPrice.intValue())+ "eur");
		sendMessage.setChatId(message.getChatId().toString());
		try {
			sendMessage(sendMessage);
			sendPhoto(sendPhoto);
		} catch (TelegramApiException e) {
		throw new RuntimeException(e);
		}
	}

}