package bilokhado.tomcatjerseyrest.dao;

import bilokhado.tomcatjerseyrest.domain.Country;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object to retrieve Country records from the DB
 */
public class CountryDao {

    private DataSource dataSource;

    public CountryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Country> getCountries() throws SQLException {
        List<Country> fetchedCountries = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Code, Name FROM Country ORDER BY Code, Name");
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                fetchedCountries.add(mapCountry(resultSet));
            }
        }
        return fetchedCountries;
    }

    public Country getCountry(String code) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Country country;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                     "SELECT Code, Name FROM Country WHERE Code=?");
             statement.setString(1, code);
             resultSet = statement.executeQuery();
             boolean hasResult = resultSet.first();
             if (hasResult)
                 country = mapCountry(resultSet);
             else
                 country = null;
        } finally {
            closeIfNotNull(resultSet, statement, connection);
        }
        return country;
    }

    private Country mapCountry(ResultSet resultSet) throws SQLException {
        Country country = new Country();
        country.setCode(resultSet.getString("Code"));
        country.setName(resultSet.getString("Name"));
        return country;
    }

    private void closeIfNotNull(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null)
                    resource.close();
            } catch (Exception ex) {
                //We are closing, so just ignore exceptions for now
            }
        }
    }

}
