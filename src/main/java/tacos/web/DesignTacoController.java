package tacos.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.model.helpers.ErrorPrinter;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientType;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;

import java.util.*;
import java.util.stream.Collectors;

import static tacos.model.helpers.Helpers.indent;

// ---
// Based on Listing 2.4 of "Spring in Action" 6th edition
// ---

@Slf4j
@Controller // Spring will create an instance of this class in the Spring application context at scan time
@RequestMapping("/design") // Kind of requests that this controller handles
@SessionAttributes("tacoOrder") // The bean stored under "tacoOrder" has session scope (is retained between requests)
public class DesignTacoController {

    private final static Random rand = new Random();
    private final IngredientRelation ingredientRelation;
    private final List<Taco> predefinedTacos;

    // @Autowired autowired annotation is not even needed anymore
    public DesignTacoController(@NotNull IngredientRelation ingredientRelation) {
        this.ingredientRelation = ingredientRelation;
        this.predefinedTacos =  Collections.unmodifiableList(makeListOfPredefinedTacos());
        log.info(">>> {} created",Helpers.makeLocator(this));
    }

    // Filling the "Session Model" so that the "ingredients" are
    // available for template processing.

    @ModelAttribute
    public void addIngredientsToModel(@NotNull Model model) {
        log.info(">>> {}.addIngredientsToModel() called with Model {}",
                Helpers.makeLocator(this),
                Helpers.makeLocator(model));
        for (IngredientType type : IngredientType.values()) {
            // Instead of ".toString().toLowercase()" use ".name().toLowercase()".
            // Lower-casing is needed because the Thymeleaf template contains the names in lowercase:
            // "${cheese}", "${veggies}" etc.
            String key = type.name().toLowerCase();
            assert IngredientType.valueOf(key.toUpperCase()) == type; // Java's valueOf() demands correct case!
            model.addAttribute(key, Collections.unmodifiableSet(ingredientRelation.getByTypeSorted(type)));
        }
    }

    // Obtain a new, empty TacoOrder instance for insertion into the (session-scoped) model

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        TacoOrder res = new TacoOrder();
        log.info(">>> {}.order(): new empty {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(res));
        return res;
    }

    // Obtain a new, empty Taco (actually, randomly initalized) instance for
    // insertion into the model
    // (The IDE is apparently aware that "th:object="${taco}" in the template is about a "Taco" instance.)

    @ModelAttribute(name = "taco")
    public @NotNull Taco taco() {
        Taco taco;
        if (predefinedTacos.isEmpty()) {
            taco = new Taco();
        } else {
            int index = rand.nextInt(predefinedTacos.size());
            taco = new Taco(predefinedTacos.get(index));
            taco.setName(taco.getName() + " " + rand.nextInt(100));
        }
        log.info(">>> {}.taco(): new {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(taco));
        return taco;
    }

    private Taco makeTaco(String name, String... stringIds) {
        Taco res = new Taco();
        res.setName(name);
        Set<Ingredient> set =
                Arrays.stream(stringIds)
                        .map(IngredientId::new)
                        .map(ingredientRelation::getById)
                        .collect(Collectors.toSet());
        res.setIngredients(set);
        return res;
    }

    private List<Taco> makeListOfPredefinedTacos() {
        List<Taco> res = new LinkedList<>();
        res.add(makeTaco("Full fat", "FLTO", "GRBF", "CHED", "JACK", "SRCR"));
        res.add(makeTaco("El veggie", "COTO", "TMTO", "LETC", "SLSA"));
        res.add(makeTaco("Just Lettuce", "FLTO", "LETC", "SRCR"));
        res.add(makeTaco("Leon the Professional",
                ingredientRelation
                        .getIngredientStream()
                        .map(Ingredient::getId)
                        .map(IngredientId::getRaw)
                        .toList()
                        .toArray(new String[]{})));
        return res;
    }

    // ------------------------------------------------------------
    // Request handling below. The initial path has been given by the class annotation @RequestMapping
    // ------------------------------------------------------------

    @GetMapping
    public @NotNull String showDesignForm() {
        log.info(">>> {}.showDesignForm()", Helpers.makeLocator(this));
        return "design";
    }

    // ----------------------
    // From Listing 2.6.
    // This is eventually called when the client POSTs to "/design" URL.
    //
    // - The "taco" is the information that was POSTed, already suitable marshalled into a "Taco" instance.
    // - The "tacoOrder" comes from the session-scoped part of the model.
    // - The "errors" argument lists bean validation errors, if any.
    //   The annotation @Valid makes Spring framework run all the validations implied by annotations
    //   on Taco fields. Any errors raised go into the "errors" object.
    //   The "errors" object is not null, even if there were no errors.
    //
    // As none of the arguments are supposed to be null, we add
    // org.jetbrains.annotations.NoNull annotations.
    //
    // For Errors, see:
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Errors.html
    //
    // You may leave out the "errors" parameter, but if so and there is a validation error,
    // Spring raises an exception, leading to HTTP ERROR 400, "Bad Request"
    // ----------------------

    @PostMapping
    public @NotNull String processTaco(@NotNull @Valid Taco taco, @NotNull Errors errors, @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info(">>> {}.processTaco()", Helpers.makeLocator(this));
        log.info(">>>>>> 'taco' argument is {}",Helpers.makeLocator(taco));
        log.info(">>>>>> 'tacoOrder' argument is {}",Helpers.makeLocator(tacoOrder));
        if (errors.hasErrors()) {
            log.info(">>>>>> 'errors' argument is {}",Helpers.makeLocator(errors));
            log.info(">>>>>> errors detail");
            String err = ErrorPrinter.printErrors(errors, Helpers.usualIndentCount);
            log.info(indent(err));
        }
        if (errors.hasErrors()) {
            return "design";
        }
        else {
            System.out.println(taco.toString());
            tacoOrder.addTaco(taco);
            return "redirect:/orders/current";
        }
    }

}
