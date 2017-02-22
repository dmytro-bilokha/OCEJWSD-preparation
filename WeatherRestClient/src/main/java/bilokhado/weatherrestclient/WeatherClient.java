package bilokhado.weatherrestclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Class represents Weather REST API client
 */
public class WeatherClient {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String API_BASE = "https://api.apixu.com/v1/";
    private static final String CURRENT_WEATHER = "current";
    private static final String FORMAT_XML = ".xml";
    private static final String FORMAT_JSON = ".json";

    private String apiKey;

    public WeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCurrentWeather(String location) throws WeatherClientException {
        String url= API_BASE + CURRENT_WEATHER + FORMAT_XML + '?'
                + "key=" + percentEncode(apiKey)
                + "&q=" + percentEncode(location);
        URLConnection  connection;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException ex) {
            throw new WeatherClientException("Unable to connect to the REST service with url='" + url + '\'', ex);
        }
        connection.setDoInput(true);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException ex) {
            throw new WeatherClientException("Unable to get input data stream from the url='" + url + '\'', ex);
        }
        StringBuilder outputBuilder = new StringBuilder();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                outputBuilder.append(line);
                outputBuilder.append(NEW_LINE);
            }
        } catch (IOException ex) {
            throw new WeatherClientException("Unable to read data from the url='" + url + '\'', ex);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                //Ignore any exception, we are anyway closing
            }
        }
        return outputBuilder.toString();
    }

    private String percentEncode(String data) throws WeatherClientException {
        try {
            return URLEncoder.encode(data, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException ex) {
            throw new WeatherClientException("Unable to URLencode UTF-8 string '" + data + "'", ex);
        }
    }

}
