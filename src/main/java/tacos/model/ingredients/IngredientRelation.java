package tacos.model.ingredients;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tacos.model.helpers.Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ---
// This is a cache for a "relation" (in the database sense) of ingredients.
// ---

@Slf4j
@Component // To be scanned by Spring
@Scope("singleton") // In scope "singleton": create only one
public class IngredientRelation  {

    // The relation is implemented by a map. Maybe the map should be immutable.
    // The map is filled at construction time either from hardcoded values or
    // from the database. It doesn't change thereafter.

    private final Map<IngredientId, Ingredient> map = new HashMap<>();

    // Create empty relation to which one can add later with addIngredientToMap().

    public IngredientRelation() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    // Create relation prefilled with all the ingredients passed.

    public IngredientRelation(@NotNull Collection<Ingredient> ingredients) {
        ingredients.forEach(this::addIngredient);
        log.info(">>> {} created from list", Helpers.makeLocator(this));
    }

    // Throws if an ingredient with the same id already exists in the map
    // Currently "protected" as called only from subclasses.

    protected void addIngredient(@NotNull Ingredient ingredient) {
        final var iid = ingredient.getId();
        final var old = map.put(iid, ingredient);
        if (old != null) {
            throw new IllegalStateException("Clash on " + iid);
        }
    }

    public @Nullable Optional<Ingredient> getById(@NotNull IngredientId id) {
        return Optional.ofNullable(map.get(id));
    }

    public @Nullable Optional<Ingredient> getById(@NotNull String id) {
        return Optional.ofNullable(map.get(new IngredientId(id)));
    }

    public @NotNull Set<Ingredient> getByType(@NotNull IngredientType type) {
        return getIngredientStream()
                .filter(ingr -> ingr.getType() == type)
                .collect(Collectors.toSet());
    }

    public @NotNull SortedSet<Ingredient> getByTypeSorted(@NotNull IngredientType type) {
        return new TreeSet<>(getByType(type));
    }

    public @NotNull Stream<IngredientId> getIdStream() {
        return map.keySet().stream();
    }

    public @NotNull Stream<Ingredient> getIngredientStream() {
        return map.values().stream();
    }

    // In order to allow the possibility of having a variable set of types,
    // get the list of available types from the relation, not from the enum.
    // TODO: Should be buffered?

    public @NotNull ArrayList<IngredientType> getTypesOccurring() {
        return getTypesOccuring(map.values());
    }

    // Get the list of types that occur in the collection of ingredients.
    // Returns a sorted array list, with the types sorted by type name.

    public static @NotNull ArrayList<IngredientType> getTypesOccuring(@NotNull Collection<Ingredient> ingredients) {
        return ingredients.stream()
                .map(Ingredient::getType)
                .distinct() // "stateful intermediate operation"
                .sorted(IngredientType::compareTo) // "stateful intermediate operation"
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Get the list of ingredients that have the given type and that occur in the collection of ingredients.
    // Returns a sorted list, with the ingredients sorted by their (plaintext) name

    public static @NotNull ArrayList<Ingredient> getIngredientsByType(@NotNull Collection<Ingredient> ingredients, @NotNull IngredientType type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType() == type)
                .distinct() // "stateful intermediate operation"
                .sorted(Comparator.comparing(Ingredient::getName)) // "stateful intermediate operation"
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
