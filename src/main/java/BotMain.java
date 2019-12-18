import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class BotMain {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botAPI = new TelegramBotsApi();

        try {
            botAPI.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
