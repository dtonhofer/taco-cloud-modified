package tacos.web.common;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import tacos.model.helpers.ErrorPrinter;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientType;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;

import java.util.Collections;
import java.util.Set;

import static tacos.model.helpers.Helpers.indent;

@Slf4j
public class Common {

    public static void addIngredientsToModel(
            @NotNull Object controller,
            @NotNull Model model,
            @NotNull IngredientRelation relation) {
        log.info(">>> {}.addIngredientsToModel() called with Model {}",
                Helpers.makeLocator(controller),
                Helpers.makeLocator(model));
        for (IngredientType type : relation.getTypesOccurring()) {
            // Instead of ".toString().toLowercase()" use ".name().toLowercase()".
            // Lower-casing is needed because the Thymeleaf template contains the
            // type names in lowercase: "${cheese}", "${veggies}" etc.
            // Otherwise there will be no match
            final String attributeName = type.name().toLowerCase();
            final Set<Ingredient> attributeValue = Collections.unmodifiableSet(relation.getByType(type));
            assert model.getAttribute(attributeName) == null : "Not yet stored in model";
            model.addAttribute(attributeName, attributeValue);
        }
    }

    // POST processing from DesignTacoController or ProposeTacoController

    public static @NotNull String processTaco(
            @NotNull @Valid Taco taco,
            @NotNull Errors errors,
            @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info(">>>>>> 'taco' argument is {}", Helpers.makeLocator(taco));
        log.info(">>>>>> {}", taco.toDetailedString());
        log.info(">>>>>> 'tacoOrder' argument is {}", Helpers.makeLocator(tacoOrder));
        if (errors.hasErrors()) {
            log.info(">>>>>> 'errors' argument is {}", Helpers.makeLocator(errors));
            log.info(">>>>>> errors detail");
            String err = ErrorPrinter.printErrors(errors, Helpers.usualIndentCount);
            log.info(indent(err));
        }
        if (errors.hasErrors()) {
            return "design";
        } else {
            // TODO: Check whether there is a name clash
            // This is a validation that cannot be done on the bean alone. How do you it?
            tacoOrder.addTaco(taco);
            return "redirect:/orders/current";
        }
    }

}
