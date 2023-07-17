package tacos.model.ingredients.source;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tacos.model.ingredients.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component // To be scanned by Spring
@Scope("singleton") // In scope "singleton": create only one
public class IngredientsSourceHardcoded implements IngredientsSource {

    private static void addIngredient(
            @NotNull List<Ingredient> ingredients,
            @NotNull String id,
            @NotNull String name,
            @NotNull IngredientType type) {
        ingredients.add(new Ingredient(new IngredientId(id), name, type));
    }

    @Override
    public IngredientRelation refresh() {
        List<Ingredient> list = new ArrayList<>();
        addIngredient(list, "FLTO", "Flour Tortilla", IngredientType.WRAP);
        addIngredient(list, "COTO", "Corn Tortilla", IngredientType.WRAP);
        addIngredient(list, "GRBF", "Ground Beef", IngredientType.PROTEIN);
        addIngredient(list, "CARN", "Carnitas", IngredientType.PROTEIN);
        addIngredient(list, "TMTO", "Diced Tomatoes", IngredientType.VEGGIES);
        addIngredient(list, "LETC", "Lettuce", IngredientType.VEGGIES);
        addIngredient(list, "CHED", "Cheddar", IngredientType.CHEESE);
        addIngredient(list, "JACK", "Monterrey Jack", IngredientType.CHEESE);
        addIngredient(list, "SLSA", "Salsa", IngredientType.SAUCE);
        addIngredient(list, "SRCR", "Sour Cream", IngredientType.SAUCE);
        return new IngredientRelation(list);
    }
}
