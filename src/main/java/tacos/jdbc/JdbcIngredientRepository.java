package tacos.jdbc;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// ---
// Code from Chapter 3.1.2 of "Spring in Action" 6th edition,
// slightly modified to use IngredientId id instead of String id.
// ---

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private final @NotNull JdbcTemplate jdbcTemplate;

    public JdbcIngredientRepository(@NotNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // "Spring in Action" returns Iterable<Ingredient> here, but why?
    // That's an extremely weak interface! Let's return List<Ingredient>.

    @Override
    public List<Ingredient> findAll() {
        final String sql = "select id, name, type from Ingredient";
        return jdbcTemplate.query(sql,JdbcIngredientRepository::mapRowToIngredient);
    }

    @Override
    public Optional<Ingredient> findById(@NotNull IngredientId id) {
        final String sql = "select id, name, type from Ingredient where id=?";
        final @NotNull List<Ingredient> results = jdbcTemplate.query(sql,JdbcIngredientRepository::mapRowToIngredient,id.getRaw());
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Ingredient save(@NotNull Ingredient ingredient) {
        final String sql = "insert into Ingredient (id, name, type) values (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                ingredient.getId().getRaw(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }

    // ---
    // Interpreted as RowMapper<Ingredient> to transform a row into an Ingredient instance
    // ---

    private static Ingredient mapRowToIngredient(@NotNull ResultSet row, int _rowNum) throws SQLException {
        final String rawType = row.getString("type");
        final String rawId = row.getString("id");
        final IngredientType type = IngredientType.valueOfCasingIndifferent(rawType); // no need to fix casing
        final IngredientId id = new IngredientId(rawId); // no need to fix casing
        final String name = row.getString("name");
        return new Ingredient(id,name,type);
    }

}
