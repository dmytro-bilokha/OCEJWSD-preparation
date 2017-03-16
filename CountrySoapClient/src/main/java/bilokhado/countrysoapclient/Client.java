package bilokhado.countrysoapclient;

import bilokhado.countrysoapclient.webservice.Country;
import bilokhado.countrysoapclient.webservice.CountryNotFoundException;
import bilokhado.countrysoapclient.webservice.SoapCountryService;
import bilokhado.countrysoapclient.webservice.SoapCountryWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOMFeature;

/**
 * Main application class to test SOAP web service
 */
public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private Client() {
        try {
            SoapCountryService service = openService();
            service.getCountries().forEach(country -> System.out.println(country.getCode() + " - " + country.getName()));
        } catch (WebServiceException ex) {
            LOG.error("Failed to get data from the web service", ex);
        }
    }

    private Client(String countryCode) {
        try {
            SoapCountryService service = openService();
            Country country = service.getCountry(countryCode);
            System.out.println(country.getCode() + " - " + country.getName());
            System.out.println("Binary data: ");
            for (byte dataElement : service.getFileData()) {
                System.out.printf("0x%02X ", dataElement);
            }
        } catch (CountryNotFoundException ex) {
            LOG.error("Got CountryNotFoundException with message='{}', faultInfo='{}'", ex.getMessage()
                , ex.getFaultInfo(), ex);
        } catch (WebServiceException ex) {
            LOG.error("Failed to get data from the web service for countryCode='{}'", countryCode, ex);
        }
    }

    private SoapCountryService openService() {
            SoapCountryWs service = new SoapCountryWs();
            service.setHandlerResolver(new ClientHandlerResolver("lookslikekeyisnotsecure"));
            return service.getSoapCountryPort(new MTOMFeature());
    }

    public static void main(String[] args) {
        if (args.length == 0)
            new Client();
        if (args.length == 1)
            new Client(args[0]);
    }
}
