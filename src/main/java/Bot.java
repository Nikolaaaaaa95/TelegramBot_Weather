
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

    public void sendMesg (Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        if (!sendMessage.getText().isEmpty()) {
            try {
                setButtons(sendMessage);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        List<KeyboardRow> keyboardRowInfo = new ArrayList<>();
        KeyboardRow keyboardRowInfoFirst = new KeyboardRow();
        KeyboardRow keyboardRowInfoSecond = new KeyboardRow();

        List<KeyboardRow> keyboardRowWeatherSettings = new ArrayList<>();
        KeyboardRow keyboardRowWeatherSettingsFirst = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Погода"));
        keyboardFirstRow.add(new KeyboardButton("Settings"));
        keyboardFirstRow.add(new KeyboardButton("Info"));


        keyboardRowInfoFirst.add(new KeyboardButton("UserName"));
        keyboardRowInfoFirst.add(new KeyboardButton("City"));
        keyboardRowInfoSecond.add(new KeyboardButton("Weather Settings"));
        keyboardRowInfoSecond.add(new KeyboardButton("Back"));

        keyboardRowWeatherSettingsFirst.add(new KeyboardButton("Сегодня"));
        keyboardRowWeatherSettingsFirst.add(new KeyboardButton("Завтра"));
        keyboardRowWeatherSettingsFirst.add(new KeyboardButton("5 дней"));

        if (sendMessage.getText().equals("Что будем настраивать?")) {
            keyboardRowInfo.add(keyboardRowInfoFirst);
            keyboardRowInfo.add(keyboardRowInfoSecond);
            replyKeyboardMarkup.setKeyboard(keyboardRowInfo);
        }
        else {
            if (sendMessage.getText().equals("На какой день вам необходима погода?")) {
                keyboardRowWeatherSettings.add(keyboardRowWeatherSettingsFirst);
                replyKeyboardMarkup.setKeyboard(keyboardRowWeatherSettings);
            }
            else {
                keyboardRowList.add(keyboardFirstRow);
                replyKeyboardMarkup.setKeyboard(keyboardRowList);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "CatRusyaBot";
    }

    @Override
    public String getBotToken() {
        return "5595946261:AAHDJk3lUfKLJvSRXlRHi-qpV-QC1HJ36ic";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String result;
        try {
            result = Service.updateMessage(message, update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendMesg(update.getMessage(), result);

    }
}
