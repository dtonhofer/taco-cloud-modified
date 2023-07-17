package tacos.model.ingredients.source;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tacos.jdbc.JdbcIngredientRepository;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientsSource;

@Slf4j
// @Component // To be scanned by Spring
@Scope("singleton") // In scope "singleton": create only one
public class IngredientsSourceJdbc implements IngredientsSource {

    private final JdbcIngredientRepository ingredientRepository;

    public IngredientsSourceJdbc(@NotNull JdbcIngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientRelation refresh() {
        return new IngredientRelation(ingredientRepository.findAll());
    }

}
