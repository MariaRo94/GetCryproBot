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

    private static final String API_BITCOIN = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
    private static final String BOT_TOKEN = "8010622979:AAE3GeyCINvdoySpHE0u03E_acwVh7YxgvQ";
    private static final String BOT_USERNAME = "CRYPTO_TRADER_BOT";

    @Override
    public String getBotUsername() {

        return BOT_USERNAME;
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

    private void sendHelpMessage(long chatId) {
        ;

    }

    private void handlePriceRequest(long chatId) {
        try {
            String price = getBitcoinPrice();
            sendMessage(chatId, "Текущий курс BTC: " + price + " USD");
            log.info("Успешное получение курса BTC для пользователя {}", chatId);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка получения курса");
            log.error("Ошибка получения курса биткоина для пользователя {}", chatId, e);
        }
    }


        private void sendWelcomeMessage(long chatId){
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

        private void sendMainMenu ( long chatId){
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
//
//        private void sendBitcoinActions ( long chatId){
//            SendMessage message = new SendMessage();
//            message.setChatId(String.valueOf(chatId));
//            message.setText("Действия с Bitcoin:");
//            message.setReplyMarkup(ButtonsFactory.createBitcoinActionsKeyboard());
//
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }

        private void handleCallbackQuery (CallbackQuery callbackQuery){
            String callbackData = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            switch (callbackData) {
                case "refresh_btc":
                    // Обработка обновления курса
                    break;
                case "history_btc":
                    // Обработка запроса истории
                    break;
                // ... другие обработчики
            }
        }

        private String getBitcoinPrice () throws IOException, InterruptedException {
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(API_BITCOIN))
                        .build();
                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());

                if (!rootNode.has("bitcoin") || !rootNode.get("bitcoin").has("usd")) {
                    throw new RuntimeException("Неверный формат ответа от API");
                }

                double price = rootNode.path("bitcoin").path("usd").asDouble();
                return String.format("%.2f USD", price);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Ошибка при получении курса Биткоина: " + e.getMessage(), e);
            }
        }


        private void sendMessage ( long chatId, String text){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String getBotToken () {
            return BOT_TOKEN;
        }
    }


