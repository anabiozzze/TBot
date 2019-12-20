package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParser {
    private String response;
    private String urlAPI;

    public JsonParser(String url) {
        urlAPI = url;
    }

    public String getResponse() {
        getWeather();
        return response;
    }

    // getting string with the required data
    private void getWeather() {
        try {
            String sts = streamToSting(getStream());
            response = parseJSON(sts);
        } catch (IOException e) {
            response = "Sorry, we out of line!";
            e.printStackTrace();
        }
    }

    private InputStream getStream() throws IOException {
        URL url = new URL(urlAPI);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream is;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }

        return is;
    }

    // "raw" string not passed parsing
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

    // parsing JSON page to the required string
    private String parseJSON(String response) {
        JSONObject allArrs = new JSONObject(response); // all arrays of JSON ("sol_keys","validity_checks", sols)
        JSONArray allSols = (JSONArray)allArrs.get("sol_keys"); // seven last sols - we need the most recent

        // Current Sol: data from all sensors (AT; HWS; PRE; WD) may not be present!
        int today =  allSols.length()-1;
        String currSolNum = (String)allSols.get(today);
        JSONObject currSol = (JSONObject)allArrs.get(currSolNum);

        // Last Sol: Just for case. All data should be present.
        int yesterday =  allSols.length()-2;
        String pastSolNum = (String)allSols.get(yesterday);
        JSONObject pastSol = (JSONObject)allArrs.get(pastSolNum);

        JSONObject temp;
        double midTemp, minTemp, maxTemp;

        // Trying to get today data, if can't - the using yesterday's data
        try {
            temp = (JSONObject)currSol.get("AT");
            midTemp = (Double)temp.get("av");
            minTemp = (Double)temp.get("mn");
            maxTemp = (Double)temp.get("mx");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Today temperature is not available yet.");
            temp = (JSONObject)pastSol.get("AT");
            midTemp = (Double)temp.get("av");
            minTemp = (Double)temp.get("mn");
            maxTemp = (Double)temp.get("mx");
        }

        JSONObject wind;
        double windSpeed;

        // Trying to get today data, if can't - the using yesterday's data
        try {
            wind = (JSONObject)currSol.get("HWS");
            windSpeed = (Double)wind.get("av");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Today wind speed is not available yet.");
            wind = (JSONObject)pastSol.get("HWS");
            windSpeed = (Double)wind.get("av");
        }

        String date;

        // Trying to get today data, if can't - the using yesterday's data
        try {
            date = (String)currSol.get("Last_UTC");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Today date and time is not available yet.");
            date = (String)pastSol.get("Last_UTC");
        }

        String time = date.substring(11, 16);
        date = date.substring(2, 10);

        String result = midTemp + "c°" +
                "\n" + windSpeed + "→" +
                "\n" + currSolNum + " sol" +
                "\n" + date + "; " + time;

        return result;
    }
}
