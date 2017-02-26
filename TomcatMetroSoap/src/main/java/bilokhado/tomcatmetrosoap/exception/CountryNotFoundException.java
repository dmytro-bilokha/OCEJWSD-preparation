package bilokhado.tomcatmetrosoap.exception;

/**
 * Class represents exception thrown by web service if no country found in the DB
 */
public class CountryNotFoundException extends CountryWsException {

    public CountryNotFoundException(String countryCode) {
        super("Country not found", "Country with code='" + countryCode + "' is not present in the database");
    }

}
