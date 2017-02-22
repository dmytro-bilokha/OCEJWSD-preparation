package bilokhado.weatherrestclient;

/**
 * Class represents exception which should be thrown in case of issues with accessing Weather REST service
 */
public class WeatherClientException extends Exception {

    public WeatherClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WeatherClientException(String message) {
        super(message);
    }
}
