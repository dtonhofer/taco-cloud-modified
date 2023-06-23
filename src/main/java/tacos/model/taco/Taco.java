package tacos.model.taco;

// import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientRelation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// ---
// This is a mutable Bean into which the framework inserts data obtained from the "taco design form".
// The Taco instance is then passed to the appropriate controller method.
// Being mutable, it can be invalid, e.g. have a null "name" and no "ingredients".
// ---

@Slf4j
@Data
public class Taco {

    // "Taco" may well need a dedicated ID, not only a "name" (?)
    // Or if the "name" is the ID, then the object that manages the Taco instances
    // (i.e. a "TacoOrder" instance) has to check for duplicate IDs.

    @jakarta.validation.constraints.NotNull // runs at validation time
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @jakarta.validation.constraints.NotNull // runs at validation time
    @Size(min=1, message="You must choose at least 1 ingredient")
    private Set<Ingredient> ingredients = new HashSet<>();

    // Let Lombok generate default constructor and getters and setters!
    //
    // However:
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
            Ingredient ing = IngredientRelation.relation.getById(id);
            if (ing == null) {
                log.warn("No ingredient corresponds to id {}", id);
            } else {
                ingredients.add(ing);
            }
        }
    }

    // A nicer Taco printout

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Taco '" + name + "'\n");
        ingredients.forEach(ingr -> buf.append("  " + ingr + "\n"));
        return buf.toString();
    }

}