package bilokhado.weatherrestclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class to test Weather REST API
 */
public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private Client(String apiKey, String location) {
        WeatherClient client = new WeatherClient(apiKey);
        try {
            System.out.println(client.getCurrentWeather(location));
        } catch (WeatherClientException ex) {
            LOG.error("Failed to get weather", ex);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Weather REST API Client. Parameters required:");
            System.out.println(" api_key");
            System.out.println(" city_name");
            System.exit(-1);
        }
        Client client = new Client(args[0], args[1]);
    }
}
