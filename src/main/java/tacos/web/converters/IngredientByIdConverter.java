package tacos.web.converters;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientRelation;

// ---
// A single instance of component is created by Spring Framework at application startup.
// It is used to convert "ingredient id", which are Strings, to proper Ingredient instances.
// If there is another Component which also implements Converter<String, Ingredient>,
// Spring Framework seems to use the first one encountered in the classpath (maybe?)
// ---

@Slf4j
@Component  // Spring will create an instance of this class in the Spring application context at scan time
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    public IngredientByIdConverter() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    @Override
    @Nullable
    public Ingredient convert(@NotNull String id) {
        var res = IngredientRelation.relation.getById(id);
        log.info(">>> {} converting {} to {}", Helpers.makeLocator(this), id, res);
        return res;
    }
}