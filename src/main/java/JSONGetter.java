import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONGetter {

    public static void main(String[] args) {
        try {
            getWeather("https://api.nasa.gov/insight_weather/?api_key=DEMO_KEY&feedtype=json&ver=1.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getWeather(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream is;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }

        String response = streamToSting(is);
        JSONObject jsonObj = new JSONObject(response);
        System.out.println(jsonObj);
    }

    private static String streamToSting(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();
        return sb.toString();
    }
}
