package tacos.model.ingredients.hardcoded;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ---
// The "relation" (in sense of "database relation" or "first-order logic relation")
// of Ingredients. It's a singleton.
//
// This is based on Chapter 2 of "Spring in Action", 6th edition, where the relation
// is hardcoded. It is moved to the relational database in Chapter 3.
// However, Chapter 2 duplicates the code for creating the internal map.
// Better have everything in a single class?
//
// Instead of creating and holding the singleton ourselves, we let Spring
// Framework do it for us. See https://www.baeldung.com/spring-bean-scopes
// ---

@Slf4j
@Component // To be scanned by Spring
@Scope("singleton") // In scope "singleton": create only one
public class IngredientRelation {

    // The relation is implemented by a map. Maybe the map should be immutable.

    private final Map<IngredientId, Ingredient> map = new HashMap<>();

    public IngredientRelation() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    private static void addIngredientToMap(@NotNull String id,
                                           @NotNull String name,
                                           @NotNull IngredientType type,
                                           @NotNull Map<IngredientId, Ingredient> map) {
        var iid = new IngredientId(id);
        var old = map.put(iid, new Ingredient(iid, name, type));
        if (old != null) {
            throw new IllegalStateException("Clash on id: " + iid);
        }
    }

    {
        addIngredientToMap("FLTO", "Flour Tortilla", IngredientType.WRAP, map);
        addIngredientToMap("COTO", "Corn Tortilla", IngredientType.WRAP, map);
        addIngredientToMap("GRBF", "Ground Beef", IngredientType.PROTEIN, map);
        addIngredientToMap("CARN", "Carnitas", IngredientType.PROTEIN, map);
        addIngredientToMap("TMTO", "Diced Tomatoes", IngredientType.VEGGIES, map);
        addIngredientToMap("LETC", "Lettuce", IngredientType.VEGGIES, map);
        addIngredientToMap("CHED", "Cheddar", IngredientType.CHEESE, map);
        addIngredientToMap("JACK", "Monterrey Jack", IngredientType.CHEESE, map);
        addIngredientToMap("SLSA", "Salsa", IngredientType.SAUCE, map);
        addIngredientToMap("SRCR", "Sour Cream", IngredientType.SAUCE, map);
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
        Set<IngredientType> set = map.values().stream().map(Ingredient::getType).collect(Collectors.toSet());
        ArrayList<IngredientType> list = new ArrayList<>(set);
        list.sort(Enum::compareTo);
        return list;
    }
}
