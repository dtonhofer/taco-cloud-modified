package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientType;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;

import java.util.Collections;

// http://localhost:8080/design

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder") // there is a TacoOrder "session backing bean" in the session model
public class DesignTacoController {

    // Filling the "Session Model" so that the ingredients are
    // available for template processing.

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        log.info("addIngredientsToModel()");
        for (IngredientType type : IngredientType.values()) {
            // Instead of .toString().toLowercase() use .name().toLowercase().
            // Lower-casing is needed because the Thymeleaf template contains the names in lowercase:
            // "${cheese}", "${veggies}" etc.
            String key = type.name().toLowerCase();
            assert IngredientType.valueOf(key) == type;
            model.addAttribute(key, Collections.unmodifiableSet(IngredientRelation.relation.getByTypeSorted(type)));
        }
    }

    // Add an empty & mutable "TacoOrder" bean to the session model

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        log.info("Empty TacoOrder instance created in DesignTacoController.order()");
        return new TacoOrder();
    }

    // Add an empty & mutable "Taco" bean to the session model.
    // This will make the IDE aware that "th:object="${taco}" in the template is about a "Taco" instance.

    @ModelAttribute(name = "taco")
    public @NotNull Taco taco() {
        log.info("Empty Taco instance created in DesignTacoController.taco()");
        return new Taco();
    }

    // ------------------------------------------------------------
    // Request handling below
    // ------------------------------------------------------------

    @GetMapping
    public String showDesignForm() {
        log.info("showDesignForm()");
        return "design";
    }

    // ----------------------
    // From Listing 2.6.
    // This is eventually called when the client POSTs to "/design" URL.
    // The "tacoOrder" is labeled as "ModelAttribute" so comes from the "session model".
    // The "taco" is the information that was POSTed, already suitable marshalled into a "Taco" instance. Nice!
    // Can "taco" or "tacoOrder" be null? It might be possible. Could the mutable DTO "taco" be invalid? Maybe!
    // ----------------------

    @PostMapping
    public String processTaco(@NotNull Taco taco, @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info("processTaco() called with taco {}", taco);
        tacoOrder.addTaco(taco);
        return "redirect:/orders/current";
    }

    /*
    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }
     */

}
