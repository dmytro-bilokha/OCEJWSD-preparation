package bilokhado.tomcatmetrosoap;

import bilokhado.tomcatmetrosoap.dao.CountryDao;
import bilokhado.tomcatmetrosoap.domain.Country;
import bilokhado.tomcatmetrosoap.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOM;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

/**
 * SOAP service for the country object
 */
@WebService(name = "SoapCountryService", portName = "SoapCountryPort", serviceName = "SoapCountryWs")
@HandlerChain(file = "/META-INF/handlers.xml")
@MTOM(threshold = 1024)
public class SoapCountryService {

    private static final Logger LOG = LoggerFactory.getLogger(SoapCountryService.class);
    private static final String FILE_PATH = "/WEB-INF/classes/appData/random.bin";

    @Resource
    private WebServiceContext context;

    private CountryDao countryDao;

    public SoapCountryService() {
        LOG.info("Instance of the SoapCountryService has been created");
    }

    @PostConstruct
    protected void init() {
        DataSource dataSource;
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/wsds");
        } catch (NamingException ex) {
            throw new IllegalStateException("Unable to get DataSource via JNDI lookup", ex);
        }
        if (dataSource == null)
            throw new IllegalStateException("Got null instead of real DataSource reference with JNDI");
        countryDao = new CountryDao(dataSource);
    }

    @WebMethod
    public List<Country> getCountries() {
        List<Country> countries = null;
        try {
           countries = countryDao.getCountries();
        } catch (SQLException ex) {
            LOG.error("Failed to get list of countries from the DB", ex);
        }
        return countries;
    }

    @WebMethod
    public Country getCountry(String code) throws CountryNotFoundException {
        Country country = null;
        try {
            country = countryDao.getCountry(code);
        } catch (SQLException ex) {
            LOG.error("Failed to get country with code='{}' from the DB", code, ex);
        }
        if (country == null)
            throw new CountryNotFoundException(code);
        return country;
    }

    @WebMethod
    public byte[] getFileData() {
        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        String filePath = servletContext.getRealPath(FILE_PATH);
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException ex) {
            LOG.error("Failed to read data from file: '{}'", FILE_PATH, ex);
            throw new WebServiceException("Internal error: unable to read file: '" + filePath + '\'');
        }
    }

}
