package bilokhado.tomcatjerseyrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to inform Jersey about which classes are web services
 */
@ApplicationPath("/")
public class RestApp extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(RestApp.class);

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> restClasses = new HashSet<>();
        restClasses.add(RestCountryService.class);
        LOG.info("Returning the set of REST service classes");
        return restClasses;
    }

}
