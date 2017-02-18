package bilokhado.simpleservletrest;

import bilokhado.simpleservletrest.dao.CountryDao;
import bilokhado.simpleservletrest.domain.Country;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.ws.http.HTTPException;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Servlet providing RESTful web service for the 'country' resource
 */
@WebServlet(name = "RestServlet", urlPatterns = "/country/*", loadOnStartup = 1)
public class RestServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(RestServlet.class);

    @Resource(name = "jdbc/wsds")
    private DataSource dataSource;
    private CountryDao countryDao;

    @Override
    public void init() {
        countryDao = new CountryDao(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseContentType type = ResponseContentType.getRequestedType(req);
        type.prepareResponse(resp);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null)
            sendAllCountries(resp.getWriter(), type);
        else
            sendCountry(pathInfo.substring(1), type, resp);
    }

    private void sendAllCountries(PrintWriter out, ResponseContentType contentType)
            throws ServletException, IOException {
        List<Country> countries = null;
        try {
            countries = countryDao.getCountries();
        } catch (SQLException ex) {
            LOG.error("Error reading country list from the DB", ex);
            throw new ServletException("Error reading country list from the DB", ex);
        }
        if (contentType == ResponseContentType.JSON)
            sendCountriesAsJson(countries, out);
        else
            sendCountriesAsXml(countries, out);
    }

    private void sendCountriesAsXml(Collection<Country> countries, PrintWriter out)
                throws IOException {
        try (ByteArrayOutputStream outXmlStream = new ByteArrayOutputStream();
             XMLEncoder encoder = new XMLEncoder(outXmlStream);){
            encoder.setExceptionListener(e -> LOG.error("Exception during marshalling object to XML", e));
            Object[] countryArray = countries.toArray();
            encoder.writeObject(countryArray);
            encoder.flush();
            out.write(outXmlStream.toString("UTF-8"));
        }
    }

    private void sendCountriesAsJson(Collection<Country> countries, PrintWriter out) {
        Gson gson = new Gson();
        out.write(gson.toJson(countries));
    }

    private void sendCountry(String code, ResponseContentType contentType
            , HttpServletResponse response) throws ServletException, IOException {
        Country country = null;
        try {
            country = countryDao.getCountry(code);
        } catch (SQLException ex) {
            LOG.error("Error reading country with code='{}' from the DB", code, ex);
            throw new ServletException("Error reading country from the DB", ex);
        }
        if (country == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Country with code='" + code + "' not found");
            return;
        }
        if (contentType == ResponseContentType.JSON) {
            sendCountryAsJson(country, response.getWriter());
        } else {
            sendCountryAsXml(country, response.getWriter());
        }
    }

    private void sendCountryAsXml(Country country, PrintWriter out) throws IOException {
        try (ByteArrayOutputStream outXmlStream = new ByteArrayOutputStream();
             XMLEncoder encoder = new XMLEncoder(outXmlStream);) {
            encoder.setExceptionListener(e -> LOG.error("Exception during marshalling object to XML", e));
            encoder.writeObject(country);
            encoder.flush();
            out.write(outXmlStream.toString("UTF-8"));
        }
    }

    private void sendCountryAsJson(Country country, PrintWriter out) {
        Gson gson = new Gson();
        out.write(gson.toJson(country));
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private enum ResponseContentType {
        XML("application/xml; charset=UTF-8"), JSON("application/json; charset=UTF-8");

        private String contentType;

        ResponseContentType(String contentType) {
            this.contentType = contentType;
        }

        public static ResponseContentType getRequestedType(HttpServletRequest request) {
            String accept = request.getHeader("accept");
            if (accept != null && accept.toLowerCase().contains("json"))
                return ResponseContentType.JSON;
            else
                return ResponseContentType.XML;
        }

        public void prepareResponse(HttpServletResponse response) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(contentType);
        }
    }
}
