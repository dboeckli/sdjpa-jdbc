package ch.dboeckli.guru.jpa.jdbc.dao;

import ch.dboeckli.guru.jpa.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author where id = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Author author = new Author();
                author.setId(id);
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));

                return author;
            }
        } catch (SQLException e) {
            log.error("Error while retrieving author by ID: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                closeConnection(resultSet, statement, connection);
            } catch (SQLException e) {
                log.error("Error while closing resources: {}", e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Author createAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long generatedId = resultSet.getLong(1);
                author.setId(generatedId);
                return author;
            } else {
                throw new SQLException("Creating author failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error while creating author: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                closeConnection(resultSet, statement, connection);
            } catch (SQLException e) {
                log.error("Error closing database resources: {}", e.getMessage(), e);
            }
        }
    }

    private void closeConnection(ResultSet resultSet, PreparedStatement statement, Connection connection) throws SQLException {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }
}
