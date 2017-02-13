package bilokhado.simpleservletrest;

import bilokhado.simpleservletrest.dao.CountryDao;
import bilokhado.simpleservletrest.domain.Country;
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
import java.util.Arrays;
import java.util.List;

/**
 * Servlet providing RESTful web service for the 'country' resource
 */
@WebServlet(name = "RestServlet", urlPatterns = "/country", loadOnStartup = 1)
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
        PrintWriter out = resp.getWriter();
        try (ByteArrayOutputStream outXmlStream = new ByteArrayOutputStream();
                XMLEncoder encoder = new XMLEncoder(outXmlStream);){
            encoder.setExceptionListener(e -> LOG.error("Exception during marshalling object to XML", e));
            List<Country> countries = countryDao.getCountries();
            Object[] countryArray = countries.toArray();
            Arrays.sort(countryArray);
            encoder.writeObject(countryArray);
            encoder.flush();
            out.write(outXmlStream.toString("UTF-8"));
        } catch (SQLException ex) {
            throw new ServletException("Failed to get country list from the DB", ex);
        }
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

}
