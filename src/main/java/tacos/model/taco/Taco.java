package tacos.model.taco;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientSpace;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// ---
// This is actually a fully mutable Data Transfer Object (DTO). Not very nice
// ---

@Slf4j
@Data
public class Taco {

    // Taco may well need a dedicated ID, not only a name

    private String name;
    private Set<Ingredient> ingredients = new HashSet<>();

    // Let Lombok generate getters and setters
    //
    // If we don't have a Converter (the "IngredientByIdConverter") to convert
    // "String" (the if an Ingredient) to "Ingredient", we need this two methods
    // below instead. Here, they have not been commented out but made unused by
    // prefixing the method names with an underscore.

    @SuppressWarnings("unused")
    public @NotNull String[] _getIngredients() {
        String[] res = ingredients.stream().map(ingredient -> ingredient.getId().getRaw()).toList().toArray(new String[0]);
        if (log.isDebugEnabled()) {
            String contents = Arrays.stream(res).collect(Collectors.joining(",", "[", "]"));
            log.debug("getIngredients() called; returning {}", contents);
        }
        return res;
    }

    @SuppressWarnings("unused")
    public void _setIngredients(@NotNull String[] in) {
        if (log.isDebugEnabled()) {
            String contents = Arrays.stream(in).collect(Collectors.joining(",", "[", "]"));
            log.debug("setIngredients() called with {}", contents);
        }
        for (String id : in) {
            Ingredient ing = IngredientSpace.space.getById(id);
            if (ing == null) {
                log.warn("No ingredient corresponds to id {}", id);
            } else {
                ingredients.add(ing);
            }
        }
    }

}