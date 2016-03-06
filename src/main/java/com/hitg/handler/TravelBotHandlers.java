package com.hitg.handler;

import java.io.IOException;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.methods.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.hitg.BotConfig;
import com.hitg.service.PhotoService;

public class TravelBotHandlers extends TelegramLongPollingBot {

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
		SendPhoto sendPhoto =new SendPhoto();
		String chatId = message.getChatId().toString(); 
		PhotoService photoService = new PhotoService();
		//String imageUrl= "https://learninglivingfreely.files.wordpress.com/2014/03/tumblr_n320ytza511rmyav1o1_500.jpg";
		String imageUrl = "http://cdn.trustedpartner.com/images/library/PalmBeachChamber2012/HomepageMedia/ADE12432-15C5-F1C2-6AEB2ECCE85E3D02/beach.png";
		String savedImageName = photoService.savePhoto(imageUrl);
	         sendMessage.setChatId(chatId);
		     sendPhoto.setCaption("Adventure.jpg");
		     sendPhoto.setNewPhoto(savedImageName, "Adventure.jpg");
		     sendPhoto.setChatId(chatId);
		     sendPhoto(sendPhoto);
//		    
//		     String userFullName= message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
//		     sendMessage.setText("How do you like it? " +userFullName );
//		     ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//		     List<List<String>> keyboarList = new ArrayList<List<String>>();
//		     List<String> innerList = new ArrayList<String>();
//		     innerList.add("1");
//		     innerList.add("2");
//		     innerList.add("3");
//		     innerList.add("4");
//		     List<String> innerList2 = new ArrayList<String>();
//		     innerList2.add("5");
//		     innerList2.add("6");
//		     innerList2.add("7");
//		     innerList2.add("8");
//		     keyboarList.add(innerList);
//		     keyboarList.add(innerList2);
//		     replyKeyboardMarkup.setKeyboard(keyboarList);
//		     sendMessage.setReplayMarkup(replyKeyboardMarkup);
//		     sendMessage(sendMessage);
//		     System.out.println(message.getText());
		     
		     
		   //  sendMessage(sendMessage);
		     } 
	}