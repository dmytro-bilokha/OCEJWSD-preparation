package bilokhado.tomcatmetrosoap.exception;

import javax.xml.ws.WebFault;

/**
 * Class represents general web service exception
 */
@WebFault(name = "CountryWsException")
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
