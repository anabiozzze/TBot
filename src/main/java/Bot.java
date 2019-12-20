import controller.Controller;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;

public class Bot extends TelegramLongPollingBot {
    private SendPhoto msg;
    Controller controller;

    public Bot(String URL) {
        msg = new SendPhoto();
        controller = new Controller(URL);
        controller.getResponse();
        controller.getImg();
    }

    // processing received messages
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        if (update.getMessage().getText().equals("/start")) {
            msg.setChatId(update.getMessage().getChatId());
            msg.setNewPhoto(new File("src/main/resources/imageResult.png"));

            try {
                sendPhoto(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    // bot name 4 telegram server
    public String getBotUsername() {
        return "@anabiozzze_bot";
    }

    // bot token 4 telegram server
    public String getBotToken() {
        return "1032392272:AAHWSg_chn6IMB5HrOfmNvswA1mWbb56YRI";
    }
}

