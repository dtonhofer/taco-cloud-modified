package tacos.jdbc.listing_3_1;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

import java.sql.*;
import java.util.List;
import java.util.Optional;

// ---
// A slightly rewritten listing 3.2 of "Spring in Action" 6th edition.
// This is not used elsewhere in the codebase.
// ---

class QueryUsingTemplate {

    private JdbcTemplate jdbcTemplate;

    public @NotNull Optional<Ingredient> findById(@NotNull String id) {
        List<Ingredient> results = jdbcTemplate.query(
                "select id, name, type from Ingredient where id=?",
                QueryUsingTemplate::mapRowToIngredient, // interpreted as RowMapper<Ingredient>
                id);
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }

    private static Ingredient mapRowToIngredient(@NotNull ResultSet row, int rowNum) throws SQLException {
        final String rawType = row.getString("type");
        final String rawId = row.getString("id");
        final IngredientType type = IngredientType.valueOf(rawType.toUpperCase());
        final IngredientId id = new IngredientId(rawId);
        final String name = row.getString("name");
        return new Ingredient(id,name,type);
    }
}