package tacos.model.taco;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientRelation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// ---
// Based on Listing 2.2 of "Spring in Action" 6th edition
// ---

// ---
// This is a mutable Java Bean from which the Spring framework reads values and into which it
// inserts values. Being mutable, it can be invalid, e.g. have a null "name" and no "ingredients".
// Getters and setters are created by Lombok via the @Data annotation.
// ---

@Slf4j
@Data
public class Taco {

    public Taco() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    public Taco(final @NotNull Taco existing) {
        this.setName(existing.getName());
        this.setIngredients(new HashSet<>(existing.getIngredients()));
        log.info(">>> {} created from existing {}", Helpers.makeLocator(this), Helpers.makeLocator(existing));
    }

    // Design problem: "Taco" may well need a dedicated ID, not only a "name".
    // Or if the "name" *is* the ID, then the object that manages the Taco instances
    // (i.e. a "TacoOrder" instance) has to check for duplicate IDs.

    @jakarta.validation.constraints.NotNull // runs at validation time
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    // "ingredients" set is replaced completely in a setter, but is initially not null

    @jakarta.validation.constraints.NotNull // runs at validation time
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private Set<Ingredient> ingredients = new HashSet<>();

    // Let Lombok generate default constructor and getters and setters!
    //
    // However:
    //
    // If we don't have a Converter (the "IngredientByIdConverter") to convert
    // "String" (the raw id an Ingredient) to "Ingredient", we need these two methods
    // below instead. Here, they have not been commented out but marked "unused" by
    // prefixing the method names with an underscore, as we have the converter.

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
        ingredients.forEach(ingr -> { buf.append("  ");buf.append(ingr);buf.append("\n"); });
        return buf.toString();
    }

}