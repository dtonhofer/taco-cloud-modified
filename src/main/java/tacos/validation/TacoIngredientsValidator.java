package tacos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.hardcoded.IngredientRelation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class TacoIngredientsValidator implements ConstraintValidator<TacoIngredients, Set<Ingredient>> {

    private final IngredientRelation ingredientRelation;

    public TacoIngredientsValidator(@NotNull IngredientRelation ingredientRelation) {
        this.ingredientRelation = ingredientRelation;
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    @Override
    public void initialize(@NotNull TacoIngredients constraintAnnotation) {
        // no need to do anything
    }

    @Override
    public boolean isValid(@NotNull Set<Ingredient> ingredients, @NotNull ConstraintValidatorContext context) {
        List<String> errors = new LinkedList<>();
        ingredientRelation.getAvailableTypes().forEach(type -> {
            List<Ingredient> forThisType = IngredientRelation.getIngredientsByType(ingredients, type);
            if (type.isMandatory() && forThisType.isEmpty()) {
                errors.add("Select at least one " + type.name().toLowerCase());
            }
            if (type.isExclusive() && forThisType.size() > 1) {
                errors.add("Select at most one " + type.name().toLowerCase());
            }
        });
        if (errors.isEmpty()) {
            return true;
        } else {
            // TODO: would it be possible to list several separate errors?
            String fullText = String.join(" & ", errors);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fullText).addConstraintViolation();
            return false;
        }
    }
}
