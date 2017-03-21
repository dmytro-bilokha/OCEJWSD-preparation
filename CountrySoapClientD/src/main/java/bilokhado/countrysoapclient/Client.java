package bilokhado.countrysoapclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;

/**
 * Main application class to test SOAP web service
 */
public class Client {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        QName serviceName = new QName("http://tomcatmetrosoap.bilokhado/", "SoapCountryWs");
        Service service = Service.create(serviceName);
        service.setHandlerResolver(new ClientHandlerResolver("lookslikekeyisnotsecure"));
        QName portName = new QName("http://tomcatmetrosoap.bilokhado/", "SoapCountryPort");
        service.addPort(portName, SOAPBinding.SOAP11HTTP_MTOM_BINDING, "http://localhost:8081/TomcatMetroSoap/");
        Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);
        SOAPBinding binding = (SOAPBinding)dispatch.getBinding();
        binding.setMTOMEnabled(true);
        SOAPMessage request = null;
        try {
            request = MessageFactory.newInstance().createMessage();
            SOAPBody reqSoapBody = request.getSOAPBody();
            QName method = new QName("http://tomcatmetrosoap.bilokhado/"
                    , "getCountries");
            SOAPElement methodName = reqSoapBody.addChildElement(method);
        } catch (SOAPException e) {
            LOG.error("Unable to create SOAP request", e);
        }
        SOAPMessage response = dispatch.invoke(request);
        try {
            response.writeTo(System.out);
        } catch (SOAPException | IOException e) {
            LOG.error("Unable to write SOAP response to the System.out stream");
        }
    }

}
