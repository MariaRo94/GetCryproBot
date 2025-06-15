package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFactory {

    public static ReplyKeyboardMarkup createMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("💰 Узнать курс BTC");  // Текст кнопки
        row1.add("📊 Другие курсы");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("ℹ️ Помощь");
        row2.add("🔄 Главное меню");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
//
//    // Создает inline-клавиатуру для операций с BTC
//    public static InlineKeyboardMarkup createBitcoinActionsKeyboard() {
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        row1.add(InlineKeyboardButton.builder()
//                .text("Обновить курс")
//                .callbackData("refresh_btc")
//                .build());
//
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//        row2.add(InlineKeyboardButton.builder()
//                .text("История цены")
//                .callbackData("history_btc")
//                .build());
//
//        rows.add(row1);
//        rows.add(row2);
//
//        markup.setKeyboard(rows);
//        return markup;
//    }

//    // Создает клавиатуру для подтверждения действий
//    public static InlineKeyboardMarkup createConfirmationKeyboard() {
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> row = new ArrayList<>();
//
//        List<InlineKeyboardButton> buttons = new ArrayList<>();
//        buttons.add(InlineKeyboardButton.builder()
//                .text("Да")
//                .callbackData("confirm_yes")
//                .build());
//        buttons.add(InlineKeyboardButton.builder()
//                .text("Нет")
//                .callbackData("confirm_no")
//                .build());
//
//        row.add(buttons);
//        markup.setKeyboard(row);
//        return markup;
//    }
}

