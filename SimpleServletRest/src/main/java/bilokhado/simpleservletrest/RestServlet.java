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
        List<Country> countries = null;
        try {
            countries = countryDao.getCountries();
        } catch (SQLException ex) {
            throw new ServletException("Error reading country list from the DB", ex);
        }
        PrintWriter out = resp.getWriter();
        String accept = req.getHeader("accept");
        if (accept != null && accept.toLowerCase().contains("json")) {
            resp.setContentType("application/json; charset=UTF-8");
            sendCountriesAsJson(countries, out);
        } else {
            resp.setContentType("application/xml; charset=UTF-8");
            sendCountriesAsXml(countries, out);
        }
        resp.setCharacterEncoding("UTF-8");
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
