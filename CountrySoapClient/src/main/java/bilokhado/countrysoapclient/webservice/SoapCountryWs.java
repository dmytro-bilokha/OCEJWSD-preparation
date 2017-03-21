
package bilokhado.countrysoapclient.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SoapCountryWs", targetNamespace = "http://tomcatmetrosoap.bilokhado/", wsdlLocation = "http://localhost:8081/TomcatMetroSoap/?wsdl")
public class SoapCountryWs
    extends Service
{

    private final static URL SOAPCOUNTRYWS_WSDL_LOCATION;
    private final static WebServiceException SOAPCOUNTRYWS_EXCEPTION;
    private final static QName SOAPCOUNTRYWS_QNAME = new QName("http://tomcatmetrosoap.bilokhado/", "SoapCountryWs");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8081/TomcatMetroSoap/?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SOAPCOUNTRYWS_WSDL_LOCATION = url;
        SOAPCOUNTRYWS_EXCEPTION = e;
    }

    public SoapCountryWs() {
        super(__getWsdlLocation(), SOAPCOUNTRYWS_QNAME);
    }

    public SoapCountryWs(WebServiceFeature... features) {
        super(__getWsdlLocation(), SOAPCOUNTRYWS_QNAME, features);
    }

    public SoapCountryWs(URL wsdlLocation) {
        super(wsdlLocation, SOAPCOUNTRYWS_QNAME);
    }

    public SoapCountryWs(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SOAPCOUNTRYWS_QNAME, features);
    }

    public SoapCountryWs(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SoapCountryWs(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SoapCountryService
     */
    @WebEndpoint(name = "SoapCountryPort")
    public SoapCountryService getSoapCountryPort() {
        return super.getPort(new QName("http://tomcatmetrosoap.bilokhado/", "SoapCountryPort"), SoapCountryService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SoapCountryService
     */
    @WebEndpoint(name = "SoapCountryPort")
    public SoapCountryService getSoapCountryPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://tomcatmetrosoap.bilokhado/", "SoapCountryPort"), SoapCountryService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SOAPCOUNTRYWS_EXCEPTION!= null) {
            throw SOAPCOUNTRYWS_EXCEPTION;
        }
        return SOAPCOUNTRYWS_WSDL_LOCATION;
    }

}