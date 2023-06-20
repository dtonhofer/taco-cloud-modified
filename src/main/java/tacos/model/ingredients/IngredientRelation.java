package tacos.model.ingredients;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ---
// The "relation" (in the database or logic sense) of Ingredients.
// It's a singleton.
// ---

public class IngredientRelation {

    // unique canonical instance (i.e. singleton) of the relation

    public final static IngredientRelation relation = new IngredientRelation();

    // The relation is implemented by a map.
    // Maybe the map should be immutable.

    private final Map<IngredientId, Ingredient> map = new HashMap<>();

    // At construction time, this is used to initialize and add a fixed set of ingredient.

    private void addIngredientToMap(@NotNull String id, @NotNull String name, @NotNull IngredientType type) {
        var iid = new IngredientId(id);
        var old = map.put(iid, new Ingredient(iid, name, type));
        if (old != null) {
            throw new IllegalStateException("Clash on id: " + iid);
        }
    }

    {
        addIngredientToMap("FLTO", "Flour Tortilla", IngredientType.WRAP);
        addIngredientToMap("COTO", "Corn Tortilla", IngredientType.WRAP);
        addIngredientToMap("GRBF", "Ground Beef", IngredientType.PROTEIN);
        addIngredientToMap("CARN", "Carnitas", IngredientType.PROTEIN);
        addIngredientToMap("TMTO", "Diced Tomatoes", IngredientType.VEGGIES);
        addIngredientToMap("LETC", "Lettuce", IngredientType.VEGGIES);
        addIngredientToMap("CHED", "Cheddar", IngredientType.CHEESE);
        addIngredientToMap("JACK", "Monterrey Jack", IngredientType.CHEESE);
        addIngredientToMap("SLSA", "Salsa", IngredientType.SAUCE);
        addIngredientToMap("SRCR", "Sour Cream", IngredientType.SAUCE);
    }

    @SuppressWarnings("unused")
    public @Nullable Ingredient getById(@NotNull IngredientId id) {
        return map.get(id);
    }

    public @Nullable Ingredient getById(@NotNull String id) {
        return map.get(new IngredientId(id));
    }

    public @NotNull Set<Ingredient> getByType(@NotNull IngredientType type) {
        return getIngredientStream().filter(ingr -> ingr.getType() == type).collect(Collectors.toSet());
    }

    public @NotNull SortedSet<Ingredient> getByTypeSorted(@NotNull IngredientType type) {
        return new TreeSet<>(getByType(type));
    }

    @SuppressWarnings("unused")
    public @NotNull Stream<IngredientId> getIdStream() {
        return map.keySet().stream();
    }


    public @NotNull Stream<Ingredient> getIngredientStream() {
        return map.values().stream();
    }
}
