package tacos.jdbc.listing_3_1;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

// ---
// A rewritten listing 3.1 of "Spring in Action" 6th edition,
// which properly uses try-with-resources.
// This is not used elsewhere in the codebase.
// ---

@Slf4j
class Query {

    private DataSource dataSource;

    public @NotNull Optional<Ingredient> findById(@NotNull String id) {
        try {
            return findById_uncaught(id);
        } catch (SQLException ex) {
            log.error("JDBC refused to work properly", ex);
        } catch (RuntimeException ex) {
            log.error("Something other than JDBC refused to work properly", ex);
        }
        // Note that the caller will be unable to distinguish a "problem" from "no record with that id"
        return Optional.empty();
    }

    private @NotNull Optional<Ingredient> findById_uncaught(@NotNull String id) throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            // we are in auto-commit mode!
            return findById_withConnection(connection, id);
        } // autoclosed
    }

    private @NotNull Optional<Ingredient> findById_withConnection(@NotNull Connection connection, @NotNull String id) throws SQLException {
        try (final PreparedStatement statement =
                     connection.prepareStatement(
                             "select id, name, type from Ingredient where id=?")) {
            return findById_withPreparedStatement(statement, id);
        } // autoclosed
    }

    // Get a single record from an open PreparedStatement

    private @NotNull Optional<Ingredient> findById_withPreparedStatement(@NotNull PreparedStatement statement, @NotNull String id) throws SQLException {
        try (final ResultSet resultSet = statement.executeQuery()) {
            statement.setString(1, id);
            return findById_withResultSet(resultSet);
        } // autoclosed (or ResultSet is closed when statement is closed)
    }

    private @NotNull Optional<Ingredient> findById_withResultSet(@NotNull ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            // Book style:
            // final Ingredient.Type type = Ingredient.Type.valueOf(resultSet.getString("type"));
            // final String id = resultSet.getString("id");
            // My modified Ingredient:
            final IngredientType type = IngredientType.valueOf(resultSet.getString("type").toUpperCase());
            final IngredientId id = new IngredientId(resultSet.getString("id"));
            final String name = resultSet.getString("name");
            return Optional.of(new Ingredient(id,name,type));
        }
        else {
            return Optional.empty();
        }
    }
}
