package com.hitg.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.methods.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.ReplyKeyboardMarkup;
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
	private static  Map<Long, Map<String,List<TripAdviserResultDTO>>> RESULT_CONTEXT = new HashMap<>();
	private static Map<Long, Integer> RESULTS_INDEX = new HashMap<>();
	private static Set<Long> SHOPH_PHOTO = new HashSet<>();
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
		String chatId = message.getChatId().toString();
	
		User user = userService.findUserByChatId(message.getChatId());
		// if we have a new user
			if  (message.getText().equals("Show next")){
				sendPhoto(user, message);
			} else if (message.getText().equals("Book")){
				sendLinks(user, message);
			}
			
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

	private void sendLinks(User user, Message message) {
		
		TripData tripData = userService.getTripData(user);
		Map<String, List<TripAdviserResultDTO>> advises = RESULT_CONTEXT.get(message.getChatId());
		String key = null;
		Iterator<String> iterator = advises.keySet().iterator();
		int maxIndex = RESULTS_INDEX.get(message.getChatId());
		
		for(int i=0; i<maxIndex-1&&iterator.hasNext(); i++){
			key = iterator.next();
		}
		TripAdviserResultDTO advise = advises.get(key).get(0);
		HomeawaySearchResult result = homeawayService.search(advise.getCountryName(), advise.getCityName(), tripData.getBudget(), tripData.getStartDate()	,tripData.getEndDate(), 1);
		String homeLink = result.getEntries().get(0).getListingUrl();
		String scannerLink= scannerService.getReferalUrl(user.getCountry(), user.getCity(),advise.getCountryName(), advise.getCityName(), tripData.getStartDate(),tripData.getEndDate());
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("Here is a link for your flight: " + scannerLink + "  and here is a link for your accomodation: " +homeLink );
		sendMessage.setChatId(message.getChatId().toString());
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	private void sendPhoto(User user, Message message) {
		
		
		PhotoService photoService = new PhotoService();
		TripData tripData = userService.getTripData(user);
		if (!RESULT_CONTEXT.containsKey(message.getChatId())){
			userService.addCategory(user,message.getText().trim() );
		  RESULT_CONTEXT.put(message.getChatId(),tripAdviserService.getAdvises(user.getCategory()));
		  RESULTS_INDEX.put(message.getChatId(), 0);
		  SHOPH_PHOTO.add(message.getChatId());
		}
		Map<String, List<TripAdviserResultDTO>> advises = RESULT_CONTEXT.get(message.getChatId());
		String key = null;
		Iterator<String> iterator = advises.keySet().iterator();
		int maxIndex = RESULTS_INDEX.get(message.getChatId());
		for(int i=0; i<=maxIndex&&iterator.hasNext(); i++){
			RESULTS_INDEX.put(message.getChatId(), RESULTS_INDEX.get(message.getChatId())+1);
			key = iterator.next();
		}
		TripAdviserResultDTO advise = advises.get(key).get(0);
		HomeawaySearchResult result = homeawayService.search(advise.getCountryName(), advise.getCityName(), tripData.getBudget(), tripData.getStartDate()	,tripData.getEndDate(), 1);
		Double homeawayPrice = (result.getEntries().get(0).getPriceRanges().get(0).getFrom()+result.getEntries().get(0).getPriceRanges().get(0).getTo())/2*(tripData.getEndDate().getDayOfYear()-tripData.getStartDate().getDayOfYear());
		Double scanenrPrice = scannerService.getMinQuotesPrice(user.getCountry(), user.getCity(),advise.getCountryName(), advise.getCityName(), tripData.getStartDate(),tripData.getEndDate());
		
		SendPhoto sendPhoto = new SendPhoto();
		
		String savedImageName = photoService.savePhoto(advise.getImageUrl());
		sendPhoto.setCaption("Adventure.jpg");
		sendPhoto.setNewPhoto(savedImageName, "Adventure.jpg");
		sendPhoto.setChatId(message.getChatId().toString());
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText(advise.getCountryName() + " " + advise.getCityName() + " " + (homeawayPrice.intValue()+scanenrPrice.intValue())+ "eur");
		sendMessage.setChatId(message.getChatId().toString());

		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<List<String>> keyboard = new ArrayList<>();
		keyboard.add(Arrays.asList("Book", "Show next"));
		replyKeyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplayMarkup(replyKeyboardMarkup);
		try {
			sendMessage(sendMessage);
			sendPhoto(sendPhoto);
		} catch (TelegramApiException e) {
		throw new RuntimeException(e);
		}
	}

}