package tacos.model.taco;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientType;
import tacos.model.ingredients.hardcoded.IngredientRelation;
import tacos.validation.TacoIngredients;
import tacos.web.common.Common;

import java.util.*;
import java.util.stream.Collectors;

// ---
// Based on Listing 2.2 of "Spring in Action" 6th edition
// ---

// ---
// This is a mutable Java Bean from which the Spring framework reads values and into which it
// inserts values. Being mutable, it can be invalid, e.g. have a null "name" and no "ingredients".
// Getters and setters are created by Lombok via the @Data annotation.
// ---

// Design problem: "Taco" may well need a dedicated ID, not only a "name".
// Or if the "name" *is* the ID, then the object that manages the Taco instances
// (i.e. a "TacoOrder" instance) has to check for duplicate IDs.

@Slf4j
@Data
@Component
public class Taco {

    // Field to make Taco database-insertable (Chapter 3.1.1)
    private Long id;

    // Added in Chapter 3.1.1
    private Date createdAt = new Date();

    public Taco() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    // The validation annotations are check at "validation" time just before the handler method is called

    @jakarta.validation.constraints.NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    // The validation annotations are check at "validation" time just before the handler method is called
    // The "ingredients" set is replaced "as a whole" in a Lombok-generated setter
    // method, but is initially a not-null, but empty Set.
    // Instead of a single @Size validation, we use a special @TacoIngredients validation

    @jakarta.validation.constraints.NotNull
    @TacoIngredients
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
    public void _setIngredients(@NotNull String[] in, IngredientRelation relation) {
        if (log.isDebugEnabled()) {
            String contents = Arrays.stream(in).collect(Collectors.joining(",", "[", "]"));
            log.debug("setIngredients() called with {}", contents);
        }
        for (String id : in) {
            Ingredient ing = relation.getById(id);
            if (ing == null) {
                log.warn("No ingredient corresponds to id {}", id);
            } else {
                ingredients.add(ing);
            }
        }
    }

    public Set<Ingredient> getIngredientsByType(@NotNull IngredientType type) {
        return Common.getIngredientsByType(ingredients,type);
    }

    // Transform a set of Ingredient into a list of Ingredient, where the
    // list items are sorted by Ingredient name (the printable cleartext)

    private List<Ingredient> setToList(@NotNull Set<Ingredient> ingredients) {
        List<Ingredient> res = new ArrayList<>(ingredients);
        res.sort(Comparator.comparing(Ingredient::getName)); // this line is amazing
        return res;
    }

    // A nicer Taco printout, but it needs the "relation"

    public String toString(@NotNull IngredientRelation relation) {
        StringBuilder buf = new StringBuilder();
        buf.append("Taco");
        if (name != null) {
            buf.append(" '");
            buf.append(name);
            buf.append("'");
        } else {
            buf.append(" (name is null)");
        }
        for (IngredientType type : relation.getAvailableTypes()) {
            Set<Ingredient> ingredientsByType = getIngredientsByType(type);
            if (!ingredientsByType.isEmpty()) {
                buf.append("\n");
                buf.append(type.toString());
                buf.append(": ");
                buf.append(setToList(ingredientsByType).stream().map(Ingredient::getName).collect(Collectors.joining(" & ")));
            }
        }
        return buf.toString();
    }

}