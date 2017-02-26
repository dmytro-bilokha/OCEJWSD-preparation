package bilokhado.tomcatmetrosoap.exception;

/**
 * Class represents general web service exception
 */
public class CountryWsException extends Exception {

    private String details;

    public CountryWsException(String message, String details) {
        super(message);
        this.details = details;
    }

    public CountryWsException(String message, String details, Throwable throwable) {
        super(message, throwable);
        this.details = details;
    }

    public String getFaultInfo() {
        return this.details;
    }

}
