package tacos.model.ingredients;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// ---
// Based on Listing 2.1 of "Spring in Action" 6th edition
// ---

@Value
public class Ingredient implements Comparable<Ingredient> {

    // these fields are actually "private final" due to the Lombok @Value annotation

    @NotNull IngredientId id;
    @NotNull String name;
    @NotNull IngredientType type;

    // Constructor is "package visible" only because all the construction is done in "IngredientRelation".

    Ingredient(@NotNull IngredientId id, @NotNull String name, @NotNull IngredientType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // It is sufficient to compare on "id". Same id? Then it's the "same ingredient".

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    // It is sufficient to hash the "id"!

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Sorting is done by name

    @Override
    public int compareTo(@NotNull Ingredient o) {
        return this.name.compareTo(o.name);
    }
}
