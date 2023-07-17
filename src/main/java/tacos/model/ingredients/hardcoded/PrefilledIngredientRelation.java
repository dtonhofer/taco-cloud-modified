package tacos.model.ingredients.hardcoded;

// ---
// The "relation" (in sense of "database relation" or "first-order logic relation")
// of Ingredients. It's a singleton.
//
// Instead of creating and holding the singleton ourselves, we let Spring
// Framework do it for us. See https://www.baeldung.com/spring-bean-scopes
// ---

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

// ---
// This is a class so that it can be injected by Spring.
// Otherwise one would just have a static method to construct a
// prefilled IngredientRelation.
// ---

@Slf4j
@Component // To be scanned by Spring
@Scope("singleton") // In scope "singleton": create only one
public class PrefilledIngredientRelation extends IngredientRelation {

    public PrefilledIngredientRelation() {
        addIngredient("FLTO", "Flour Tortilla", IngredientType.WRAP);
        addIngredient("COTO", "Corn Tortilla", IngredientType.WRAP);
        addIngredient("GRBF", "Ground Beef", IngredientType.PROTEIN);
        addIngredient("CARN", "Carnitas", IngredientType.PROTEIN);
        addIngredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES);
        addIngredient("LETC", "Lettuce", IngredientType.VEGGIES);
        addIngredient("CHED", "Cheddar", IngredientType.CHEESE);
        addIngredient("JACK", "Monterrey Jack", IngredientType.CHEESE);
        addIngredient("SLSA", "Salsa", IngredientType.SAUCE);
        addIngredient("SRCR", "Sour Cream", IngredientType.SAUCE);
    }

    // Helper for the factory call, basically exists for easier typing

    private void addIngredient(@NotNull String id,
                               @NotNull String name,
                               @NotNull IngredientType type) {
        final var iid = new IngredientId(id);
        addIngredient(new Ingredient(iid, name, type));
    }

}
