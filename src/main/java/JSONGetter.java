import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONGetter {

    public String getWeather(String apiUrl) {
        try {
            return getResponse(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public String getResponse(String apiUrl) throws IOException {
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
        String result = parseJSON(response);
        System.out.println(result);

        return result;
    }

    private String streamToSting(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();
        return sb.toString();
    }

    private String parseJSON(String response) {
        JSONObject allArrs = new JSONObject(response); // all arrays of JSON ("sol_keys","validity_checks", sols)
        JSONArray allSols = (JSONArray)allArrs.get("sol_keys"); // seven last sols - we need the most recent

        int last =  allSols.length()-1;
        String lastSolNum = (String)allSols.get(last);

        JSONObject lastSol = (JSONObject)allArrs.get(lastSolNum);
        JSONObject temp = (JSONObject)lastSol.get("AT");
        JSONObject wind = (JSONObject)lastSol.get("HWS");

        double midTemp = (Double)temp.get("av");
        double minTemp = (Double)temp.get("mn");
        double maxTemp = (Double)temp.get("mx");
        double windSpeed = (Double)wind.get("av");

        return "Sol: " + lastSolNum + ", Temp: MIDDLE " + midTemp + ", MIN " + minTemp + ", MAX  " + maxTemp
                + ". Wind speed: " + windSpeed;
    }
}
