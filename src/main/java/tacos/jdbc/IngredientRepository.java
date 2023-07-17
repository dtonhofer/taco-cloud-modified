package tacos.jdbc;

import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;

import java.util.List;
import java.util.Optional;

// ---
// Code from Chapter 3.1.2 of "Spring in Action" 6th edition,
// slightly modified to use IngredientId id instead of String id.
// ---

public interface IngredientRepository {

    // "Spring in Action" returns Iterable<Ingredient> here, but why?
    // That's an extremely weak interface! Let's return List<Ingredient>.

    List<Ingredient> findAll();

    Optional<Ingredient> findById(IngredientId id);

    Ingredient save(Ingredient ingredient);

}