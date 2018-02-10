package externalApi;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * A helper class for retrieving data from end point
 */

public class JsonRetrievalService {

    public static final String BASE_URL = "https://biking.michael-simons.eu/api";
    private static final Logger logger = Logger.getLogger(JsonRetrievalService.class.getName());


    public static JsonArray readDataFromEmr(String endPoint) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) getApiEndpoint(endPoint).openConnection();

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = br.readLine()) != null) {
            sb.append(line);

        }
        br.close();

        JsonArray jsonArray = JsonRetrievalService.jsonFromString(sb.toString());
        return jsonArray;
    }

    protected static URL getApiEndpoint(final String endpoint) {
        URL hlp = null;
        try {
            hlp = new URL(String.format("%s%s", BASE_URL, endpoint));
            System.out.println("Here is the full URL: " + hlp.toString());
        } catch (MalformedURLException e) {
            // I hope so ;)
            throw new RuntimeException(e);
        }
        return hlp;
    }

    public static JsonArray jsonFromString(String jsonObjectStr) {

        JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        return object;
    }

}