package bilokhado.tomcatmetrosoap;

import bilokhado.tomcatmetrosoap.dao.CountryDao;
import bilokhado.tomcatmetrosoap.domain.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * SOAP service for the country object
 */
@WebService
public class SoapCountryService {

    private static final Logger LOG = LoggerFactory.getLogger(SoapCountryService.class);

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
    public Country getCountry(String code) {
        Country country = null;
        try {
            country = countryDao.getCountry(code);
        } catch (SQLException ex) {
            LOG.error("Failed to get country with code='{}' from the DB", code, ex);
        }
        return country;
    }

}
