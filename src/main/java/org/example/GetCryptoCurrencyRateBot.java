package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//todo добавить логирование slf4j
//todo связать кнопки и основное меню
//todo добавить получение курса Соланы
//todo добавить получение курса Этериума
//todo добавить обработку исключений в случае если нет связи с апишкой\
//todo добавить юнит тесты
//todo развернуть в докере
//todo сделать сборки попробовать
//todo подумать что можно ще добавить из функционала с криптой


@Slf4j
public class GetCryptoCurrencyRateBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "8010622979:AAE3GeyCINvdoySpHE0u03E_acwVh7YxgvQ";
    private static final String BOT_USERNAME = "CRYPTO_TRADER_BOT";
    private final CryptoPriceService cryptoPriceService;

    public GetCryptoCurrencyRateBot() {
        this.cryptoPriceService = new CryptoPriceService();
    }

    @Override
    public String getBotUsername() {

        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText.toLowerCase()) {
                case "/start":
                case "🔄 Главное меню":
                    sendMainMenu(chatId);
                    sendWelcomeMessage(chatId);
                    log.info("Пользователь {} начал работу с ботом", chatId);
                    break;

                case "/pricebtc":
                case "💰 Узнать курс BTC":
                    handlePriceRequest(chatId);
                    log.info("Сделан запрос на получение курса биткоина");
                    break;

                case "/help":
                case "ℹ️ помощь":
                case "помощь":
                    sendHelpMessage(chatId);
                    break;

                default:
                    sendMessage(chatId, "Используйте кнопки или команды:\n" +
                            "/start - Главное меню\n" +
                            "/pricebtc - Курс BTC\n" +
                            "/help - Помощь");
                    break;
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendHelpMessage(long chatId) {
        ;

    }


    private void handlePriceRequest(long chatId) {
        try {
            String price = cryptoPriceService.getBitcoinPrice();
            sendMessage(chatId, "Текущий курс BTC: " + price);
            log.info("Успешное получение курса BTC для пользователя {}", chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка получения курса");
            log.error("Ошибка получения курса биткоина для пользователя {}", chatId, e);
        }
    }


    private void sendWelcomeMessage(long chatId) {
        String welcomeText = "Привет! Добро пожаловать в CryptoBot!\n\n" +
                "Я помогу вам отслеживать курсы криптовалют.\n" +
                "Используйте кнопки ниже или команды:\n" +
                "/pricebtc - текущий курс BTC\n" +
                "/help - справка по боту";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(welcomeText);
        message.setReplyMarkup(ButtonsFactory.createMainMenuKeyboard());

        try {
            execute(message);
            log.info("Запуск бота прошел успешно");
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки приветствия", e);
        }
    }

    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");
        message.setReplyMarkup(ButtonsFactory.createMainMenuKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}


