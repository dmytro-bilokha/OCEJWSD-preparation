package bilokhado.simpleservletrest.dao;

import bilokhado.simpleservletrest.domain.Country;

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
             PreparedStatement statement = connection.prepareStatement("SELECT Code, Name FROM Country");
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                Country country = new Country();
                country.setCode(resultSet.getString("Code"));
                country.setName(resultSet.getString("Name"));
                fetchedCountries.add(country);
            }
        }
        return fetchedCountries;
    }
}
