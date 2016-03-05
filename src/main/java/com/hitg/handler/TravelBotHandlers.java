package com.hitg.handler;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.methods.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.hitg.BotConfig;

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
					handleIncomingMessage(message);
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

	private void handleIncomingMessage(Message message) throws TelegramApiException {
		SendMessage sendMessage = new SendMessage();
		SendPhoto sendPhoto =new SendPhoto();
		String chatId = message.getChatId().toString(); 
	         sendMessage.setChatId(chatId);
	         sendMessage.setText("http://embassysuites3.hilton.com/resources/media/es/DSIESES/en_US/img/shared/full_page_image_gallery/main/ES_beach001_12_712x342_FitToBoxSmallDimension_Center.jpg");
	         sendMessage.setDisableWebPagePreview(false);
	         sendMessage(sendMessage);
	}

}
