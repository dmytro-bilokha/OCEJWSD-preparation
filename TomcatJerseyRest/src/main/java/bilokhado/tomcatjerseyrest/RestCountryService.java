package bilokhado.tomcatjerseyrest;

import bilokhado.tomcatjerseyrest.dao.CountryDao;
import bilokhado.tomcatjerseyrest.domain.CountryList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * REST service for the country object
 */
@Path("/country")
public class RestCountryService {

    private static final Logger LOG = LoggerFactory.getLogger(RestCountryService.class);

    @Context
    private ServletContext servletContext;
    private CountryDao countryDao;

    public RestCountryService() {
        LOG.info("Instance of the RestCountryService has been created");
    }

    @PostConstruct
    public void init() {
        DataSource dataSource = (DataSource) servletContext.getAttribute("datasource");
        countryDao = new CountryDao(dataSource);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCountries() {
        try {
            return Response.ok(new CountryList(countryDao.getCountries())).build();
        } catch (SQLException ex) {
            LOG.error("Failed to get country list from the DB", ex);
            return Response.serverError().entity("Failed to get country list from the DB").build();
        }
    }

}
