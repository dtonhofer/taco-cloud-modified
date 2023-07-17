package tacos.model.ingredients.hardcoded;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ---
// The "relation" (in sense of "database relation" or "first-order logic relation")
// of Ingredients. Works as a cache for database contents.
//
// This is based on Chapter 2 of "Spring in Action", 6th edition, where the relation
// is hardcoded. It is moved to the relational database in Chapter 3.
// However, Chapter 2 duplicates the code for creating the internal map.
// Better have everything in a single class?
// ---

@Slf4j
public class IngredientRelation {

    // The relation is implemented by a map. Maybe the map should be immutable.

    private final Map<IngredientId, Ingredient> map = new HashMap<>();

    // Create empty relation to which one can add later with addIngredientToMap()
    // Private for now.

    public IngredientRelation() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    // Create relation prefilled with all the ingredients passed.

    public IngredientRelation(@NotNull Collection<Ingredient> ingredients) {
        ingredients.forEach(this::addIngredient);
        log.info(">>> {} created from list", Helpers.makeLocator(this));
    }

    // Throws if an ingredient with the same id already exists in the map

    protected void addIngredient(@NotNull Ingredient ingredient) {
        final var iid = ingredient.getId();
        final var old = map.put(iid, ingredient);
        if (old != null) {
            throw new IllegalStateException("Clash on " + iid);
        }
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

    // In order to allow the possibility of having a variable set of types,
    // get the list of available types from the relation, not from the enum!
    // TODO: This should be buffered.

    public @NotNull List<IngredientType> getAvailableTypes() {
        return getAvailableTypes(map.values());
    }

    public static @NotNull List<IngredientType> getAvailableTypes(@NotNull Collection<Ingredient> ingredients) {
        // this stream expression proposed by the IDE is on another level
        return ingredients.stream()
                .map(Ingredient::getType)
                .distinct() // "stateful intermediate operation"
                .sorted(Enum::compareTo) // "stateful intermediate operation"
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // returns a sorted list, with the ingredients sorted by their (plaintext) name
    public static @NotNull List<Ingredient> getIngredientsByType(@NotNull Collection<Ingredient> ingredients, @NotNull IngredientType type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType() == type)
                .distinct()
                .sorted(Comparator.comparing(Ingredient::getName))
                .toList();
    }

    // returns a sorted list, with the types sorted by the natural order (enum order)

    public static @NotNull List<IngredientType> getTypesPresent(@NotNull Collection<Ingredient> ingredients) {
        return ingredients.stream()
                .map(Ingredient::getType)
                .distinct()
                .sorted(IngredientType::compareTo) // the "compareTo" of Enum<T>
                .toList();
    }
}
