package tacos.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientType;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientsSource;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;
import tacos.web.common.Common;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller // Spring will create an instance of this class in the Spring application context at scan time
@RequestMapping("/propose") // Kind of requests that this controller handles
@SessionAttributes("tacoOrder") // The bean stored under "tacoOrder" has session scope (is retained between requests)
public class ProposeTacoController {

    private final static Random rand = new Random();

    private final IngredientRelation ingredientRelation;

    public ProposeTacoController(@NotNull IngredientsSource ingredientSource) {
        this.ingredientRelation = ingredientSource.refresh();
        log.info(">>> {} created with ingredientRelation {}",
                Helpers.makeLocator(this),
                Helpers.makeLocator(ingredientRelation));
    }

    // Filling the "Session Model" so that the "ingredients" are
    // available for template processing.

    @ModelAttribute
    public void addIngredientsToModel(@NotNull Model model) {
        Common.addIngredientsToModel(this, model, ingredientRelation);
    }

    // Obtain a new, empty TacoOrder instance for insertion into the model.
    // The class is annotated with @SessionAttributes("tacoOrder") so the TacoOrder
    // will go into the session-scoped model.

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        TacoOrder res = new TacoOrder();
        log.info(">>> {}.order(): new empty {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(res));
        return res;
    }

    // Obtain a new, randomly filled Taco instance for insertion into the model.
    // It is actually dangerous to name a method "taco" - IDE autcompletion
    // may write "taco()." instead of "taco." ...

    @ModelAttribute(name = "taco")
    public @NotNull Taco taco() {
        Taco res = new Taco();
        res.setIngredients(proposeIngredients());
        res.setName(proposeName());
        log.info(">>> {}.taco(): new {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(res));
        log.info(res.toDetailedString()); // prints taco details
        return res;
    }

    private @NotNull Set<Ingredient> proposeIngredients() {
        final List<IngredientType> types = ingredientRelation.getTypesOccurring();
        final Set<Ingredient> res = new HashSet<>();
        for (IngredientType type : types) {
            final List<Ingredient> available = new ArrayList<>(ingredientRelation.getByType(type));
            final int atMost = type.isExclusive() ? 1 : available.size();
            final int atLeast = type.isMandatory() ? 1 : 0;
            assert atLeast <= atMost && atMost <= available.size();
            final int howMany = atLeast + rand.nextInt(atMost - atLeast + 1);
            assert atLeast <= howMany && howMany <= atMost;
            // shuffle the ingredients in place and select the "howMany" first
            Collections.shuffle(available);
            Set<Ingredient> forThisType = available.stream().limit(howMany).collect(Collectors.toSet());
            res.addAll(forThisType);
        }
        return res;
    }

    private static @NotNull String proposeName() {
        final String[] names = new String[]{
                "Jester",
                "Touchdown",
                "Steamroller",
                "Clean Slate",
                "Boomstick",
                "Beanstalk",
                "Elephant",
                "Brown Garden",
                "Desert Avalanche",
                "Pink Rhino",
                "Hotcake",
                "Pigstick",
                "Dreamstate",
                "Surprise Party",
                "Resurrection",
                "Lunar Eclipse",
                "Snowslide",
                "Jungle Citadel",
                "Ocean Rhino",
                "Hammer"};
        final int randomInt = rand.nextInt(99) + 1;
        final String randomName = names[rand.nextInt(names.length)];
        return randomName + " " + randomInt;
    }

    // ------------------------------------------------------------
    // Request handling below. The initial path has been given by the class annotation @RequestMapping
    // ------------------------------------------------------------

    @GetMapping
    public @NotNull String showDesignForm() {
        log.info(">>> {}.showDesignForm()", Helpers.makeLocator(this));
        return "design";
    }

    @PostMapping
    public @NotNull String processTaco(
            @NotNull @Valid Taco taco,
            @NotNull Errors errors,
            @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info(">>> {}.processTaco()", Helpers.makeLocator(this));
        return Common.processTaco(taco, errors, tacoOrder);
    }

}
