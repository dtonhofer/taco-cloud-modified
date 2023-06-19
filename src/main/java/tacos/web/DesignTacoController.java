package tacos.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientType;
import tacos.model.ingredients.IngredientSpace;
import tacos.model.taco.Taco;
import tacos.model.tacoorder.TacoOrder;

import java.util.Collections;
import java.util.Set;

// http://localhost:8080/design

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    // ---------------- filling the SESSION MODEL

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        log.info("addIngredientsToModel()");
        for (IngredientType type : IngredientType.values()) {
            // Instead of .toString().toLowercase() use .name().toLowercase().
            // Lowercasing is needed because the Thymeleaf template contains the names in lowercase:
            // "${cheese}", "${veggies}" etc.
            String key = type.name().toLowerCase();
            assert IngredientType.valueOf(key) == type;
            model.addAttribute(key, Collections.unmodifiableSet(IngredientSpace.space.getByTypeSorted(type)));
        }
    }

    // Add an initially empty mutable TacoOrder to the SESSION MODEL

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        return new TacoOrder();
    }

    // Add an initially empty mutable Taco to the SESSION MODEL

    @ModelAttribute(name = "taco")
    public @NotNull Taco taco() {
        return new Taco();
    }

    // ------------------------------------------------------------

    @GetMapping
    public String showDesignForm() {
        log.info("showDesignForm()");
        return "design";
    }

    // ----------------------

    /*
    @PostMapping
    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
        log.info("processTaco()");
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }
    */

    // ----------------------

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }

}
