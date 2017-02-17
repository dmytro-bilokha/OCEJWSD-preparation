package bilokhado.tomcatjerseyrest;

import bilokhado.tomcatjerseyrest.dao.CountryDao;
import bilokhado.tomcatjerseyrest.domain.Country;
import bilokhado.tomcatjerseyrest.domain.CountryList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * REST service for the country object
 */
@Path("country")
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
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Failed to get country list from the DB").build();
        }
    }

    @Path("{code: [a-zA-Z]{3}}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCountry(@PathParam("code") String code) {
        try {
            Country country = countryDao.getCountry(code);
            if (country == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity("Not found country with code='" + code + '\'')
                        .build();
            return Response.ok(country).build();
        } catch (SQLException ex) {
            LOG.error("Failed to get country with code='{}' from the DB", code, ex);
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Failed to get country with code='" + code + "' from the DB")
                    .build();
        }
    }

    @Path("{code: [a-zA-Z]{3}}")
    @POST
    @Consumes({MediaType.TEXT_PLAIN})
    public Response addCountry(@PathParam("code") String code, String name) {
        try {
            if (countryDao.getCountry(code) != null)
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity("Country with code='" + code + "' already exists")
                        .build();
            Country country = new Country();
            country.setCode(code.toUpperCase());
            country.setName(name);
            countryDao.persistCountry(country);
            return Response.ok("Persisted country: " + country, MediaType.TEXT_PLAIN_TYPE).build();
        } catch (SQLException ex) {
            LOG.error("Failed to persist country with code='{}' in the DB", code, ex);
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Failed to persist country with code='" + code + "' in the DB")
                    .build();
        }
    }

}
