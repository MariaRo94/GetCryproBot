package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args) {

        GetCryptoCurrencyRateBot bot = new GetCryptoCurrencyRateBot();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Бот запущен и готов к работе!");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    }
