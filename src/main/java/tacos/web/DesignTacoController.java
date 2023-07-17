package tacos.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientsSource;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;
import tacos.web.common.Common;

// ---
// Based on Listing 2.4 of "Spring in Action" 6th edition
// ---

@Slf4j
@Controller // Spring will create an instance of this class in the Spring application context at scan time
@RequestMapping("/design") // Kind of requests that this controller handles
@SessionAttributes("tacoOrder") // The bean stored under "tacoOrder" has session scope (is retained between requests)
public class DesignTacoController {

    private final IngredientRelation ingredientRelation;

    // Constructor called by Spring at startup time

    public DesignTacoController(@NotNull IngredientsSource ingredientSource) {
        this.ingredientRelation = ingredientSource.refresh();
        log.info(">>> {} created with ingredientRelation {}",
                Helpers.makeLocator(this),
                Helpers.makeLocator(ingredientSource));
    }

    // Filling the (request-scoped) session model so that the ingredients are
    // available for template processing by Thymeleaf.

    @ModelAttribute
    public void addIngredientsToModel(@NotNull Model model) {
        Common.addIngredientsToModel(this, model, ingredientRelation);
    }

    // Obtain a new, empty TacoOrder instance for insertion into the session model.
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

    // Obtain a new, empty Taco instance for insertion into the session model.
    // (The IDE is aware that "th:object="${taco}" in the template is about a "Taco" instance. Neat!)

    @ModelAttribute(name = "taco")
    public @NotNull Taco taco() {
        Taco res = new Taco();
        log.info(">>> {}.taco(): new {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(res));
        return res;
    }

    // ------------------------------------------------------------
    // Request handling below. The initial path has been given by the class annotation @RequestMapping
    // ------------------------------------------------------------

    // ----------------------
    // Handle GET.
    // ----------------------

    @GetMapping
    public @NotNull String showDesignForm() {
        log.info(">>> {}.showDesignForm()", Helpers.makeLocator(this));
        return "design";
    }

    // ----------------------
    // Handle POST
    //
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
    public @NotNull String processTaco(
            @NotNull @Valid Taco taco,
            @NotNull Errors errors,
            @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info(">>> {}.processTaco()", Helpers.makeLocator(this));
        return Common.processTaco(taco, errors, tacoOrder);
    }

}
