package tacos.model.ingredients;

// ---
// Type of Ingredient. Named "IngredientType" for clarity and move to the
// toplevel for ease of use. Why bury it inside Ingredient class?
// ---

import org.jetbrains.annotations.NotNull;

public enum IngredientType {

    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE;

    // ---
    // Note IngredientType.valueOf(x) only works if the casing of x corresponds
    // to IngredientType's identifier casing. Thus, a method that is "casing indifferent".
    // ---

    public static IngredientType valueOfCasingIndifferent(@NotNull String x) {
        return valueOf(x.toUpperCase());
    }

    // An ingredient of this type is mandatory to make a successful Taco

    public boolean isMandatory() {
        return this == WRAP;
    }

    // Can only have 1 of this type

    public boolean isExclusive() {
        return this == WRAP || this == SAUCE;
    }

}
