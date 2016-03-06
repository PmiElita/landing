package com.hitg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import com.hitg.handler.TravelBotHandlers;
import com.hitg.service.SkyScannerService;

@SpringBootApplication
public class BotEngineApplication {

	public static ApplicationContext CONTEXT;
	
	
	public static void main(String[] args) {
		CONTEXT= SpringApplication.run(BotEngineApplication.class, args);
		
		CONTEXT.getBean(SkyScannerService.class).getCountries();;
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TravelBotHandlers());
        } catch (TelegramApiException e) {
           
        }
	}
}
