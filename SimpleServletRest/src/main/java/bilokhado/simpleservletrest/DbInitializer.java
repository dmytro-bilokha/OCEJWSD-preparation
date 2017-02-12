package bilokhado.simpleservletrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class represents listener which should init DB on application startup
 */
@WebListener
public class DbInitializer implements ServletContextListener {

    private static final String DROP_SCRIPT = "/META-INF/sql_scripts/drop.sql";
    private static final String CREATE_SCRIPT = "/META-INF/sql_scripts/create.sql";
    private static final String LOAD_SCRIPT = "/META-INF/sql_scripts/load.sql";
    private static final String STATEMENT_DELIMITER = ";";

    private static final Logger LOG = LoggerFactory.getLogger(DbInitializer.class);

    @Resource(name = "jdbc/wsds")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initializing DB...");
        int statementCounter;
        statementCounter = executeSqlScript(DROP_SCRIPT);
        LOG.info("Executed {} drop statements", statementCounter);
        statementCounter = executeSqlScript(CREATE_SCRIPT);
        LOG.info("Executed {} create statements", statementCounter);
        statementCounter = executeSqlScript(LOAD_SCRIPT);
        LOG.info("Executed {} insert statements", statementCounter);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //Just ignore destroying the context event
    }

    private int executeSqlScript(String scriptFileName) {
        int statementCounter = 0;
        try (BufferedReader scriptReader = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(scriptFileName)
                        , Charset.forName("UTF-8")));
             Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();) {
            StringBuilder sqlStringBuilder = new StringBuilder();
            for (String lineRead; (lineRead = scriptReader.readLine()) != null;) {
                lineRead = lineRead.trim();
                if (lineRead.isEmpty())
                    continue;
                if (lineRead.endsWith(STATEMENT_DELIMITER)) {
                    sqlStringBuilder.append(lineRead.substring(0, lineRead.lastIndexOf(STATEMENT_DELIMITER)));
                    statement.execute(sqlStringBuilder.toString());
                    statementCounter++;
                    sqlStringBuilder = new StringBuilder();
                } else {
                    sqlStringBuilder.append(lineRead);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to execute SQL script '" + scriptFileName
                    + "', because of I/O exception", ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to execute SQL script '" + scriptFileName
                    + "', because of SQL exception", ex);
        }
        return statementCounter;
    }
}
