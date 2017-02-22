package bilokhado.weatherrestclient;

import bilokhado.weatherrestclient.domain.CurrentWeather;
import bilokhado.weatherrestclient.domain.webmodel.Current;
import bilokhado.weatherrestclient.domain.webmodel.WebCurrentWeather;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Class represents Weather REST API client
 */
public class WeatherClient {

    private static final String API_BASE = "https://api.apixu.com/v1/";
    private static final String CURRENT_WEATHER = "current";
    private static final String FORMAT_XML = ".xml";
    private static final String FORMAT_JSON = ".json";

    private String apiKey;

    public WeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public CurrentWeather getCurrentWeather(String location) throws WeatherClientException {
        String url= API_BASE + CURRENT_WEATHER + FORMAT_XML + '?'
                + "key=" + percentEncode(apiKey)
                + "&q=" + percentEncode(location);
        InputStream inputStream = getConnectionInputStream(url);
        try {
            return parseCurrentWeather(inputStream);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                //Ignore, because we are closing anyway
            }
        }
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

    private InputStream getConnectionInputStream(String url) throws WeatherClientException {
        URLConnection  connection;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException ex) {
            throw new WeatherClientException("Unable to connect to the REST service with url='" + url + '\'', ex);
        }
        connection.setDoInput(true);
        try {
            return new BufferedInputStream(connection.getInputStream());
        } catch (IOException ex) {
            throw new WeatherClientException("Unable to get input data stream from the url='" + url + '\'', ex);
        }
    }

    //TODO: add error handling when query is invalid. In such case web service returns error object
    //with error code and description. It would be great to catch it and parse. Now it just fails on unmarshalling
    private CurrentWeather parseCurrentWeather(InputStream inputStream) throws WeatherClientException {
        WebCurrentWeather webCurrentWeather;
        try {
            JAXBContext ctx = JAXBContext.newInstance(WebCurrentWeather.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            webCurrentWeather = (WebCurrentWeather) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException ex) {
            throw new WeatherClientException("Failed to parse XML from web service", ex);
        }
        Current current = webCurrentWeather.getCurrent();
        return CurrentWeather.getBuilder()
                .locationName(webCurrentWeather.getLocation().getName())
                .countryName(webCurrentWeather.getLocation().getCountry())
                .temperature(current.getTempC())
                .humidity(current.getHumidity())
                .pressure(current.getPressureMillibars())
                .windSpeed(current.getWindKph())
                .windDegree(current.getWindDegree())
                .precipitation(current.getPrecipitationMm())
                .visibilityDistance(current.getVisibilityKm())
                .feelsLike(current.getFeelsLikeC())
                .condition(current.getCondition().getText())
                .lastUpdatedEpoch(current.getLastUpdatedEpoch())
                .build();
    }

}
