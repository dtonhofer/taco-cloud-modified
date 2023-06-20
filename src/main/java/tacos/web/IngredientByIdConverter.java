package tacos.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientRelation;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    @Override
    @Nullable
    public Ingredient convert(@NotNull String id) {
        return IngredientRelation.relation.getById(id);
    }
}