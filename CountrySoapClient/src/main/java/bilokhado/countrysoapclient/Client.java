package bilokhado.countrysoapclient;

import bilokhado.countrysoapclient.webservice.SoapCountryService;
import bilokhado.countrysoapclient.webservice.SoapCountryServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class to test SOAP web service
 */
public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private Client(String endpoint) {
        SoapCountryServiceService service = new SoapCountryServiceService();
        SoapCountryService port = service.getSoapCountryServicePort();
        System.out.println(port.getCountries());
    }

    private Client(String endpoint, String countryCode) {
        SoapCountryServiceService service = new SoapCountryServiceService();
        SoapCountryService port = service.getSoapCountryServicePort();
        System.out.println(port.getCountry(countryCode));
    }

    public static void main(String[] args) {
        if (args.length != 1 && args.length != 2) {
            System.out.println("SOAP web service client. Parameters required:");
            System.out.println(" endpoint_url");
            System.out.println("Optional parameters:");
            System.out.println(" country_code");
            System.exit(-1);
        }
        if (args.length == 1)
            new Client(args[0]);
        if (args.length == 2)
            new Client(args[0], args[1]);
    }
}
