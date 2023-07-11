package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tacos.model.helpers.Helpers;
import tacos.model.ingredients.Ingredient;
import tacos.model.ingredients.IngredientId;
import tacos.model.ingredients.IngredientRelation;
import tacos.model.ingredients.IngredientType;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;

import java.util.*;
import java.util.stream.Collectors;

// ---
// Based on Listing 2.4 of "Spring in Action" 6th edition
// ---

// Connect to: http://localhost:8080/design

@Slf4j
@Controller // Spring will create an instance of this class in the Spring application context at scan time
@RequestMapping("/design") // Kind of requests that this controller handles
@SessionAttributes("tacoOrder") // The bean stored under "tacoOrder" has session scope (is retained between requests)
public class DesignTacoController {

    private final static List<Taco> predefinedTacos = Collections.unmodifiableList(makeListOfPredefinedTacos());
    private final static Random rand = new Random();

    public DesignTacoController() {
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
            assert IngredientType.valueOf(key) == type;
            model.addAttribute(key, Collections.unmodifiableSet(IngredientRelation.relation.getByTypeSorted(type)));
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

    private static Taco makeTaco(String name, String... stringIds) {
        Taco res = new Taco();
        res.setName(name);
        Set<Ingredient> set =
                Arrays.stream(stringIds)
                        .map(IngredientId::new)
                        .map(IngredientRelation.relation::getById)
                        .collect(Collectors.toSet());
        res.setIngredients(set);
        return res;
    }

    private static List<Taco> makeListOfPredefinedTacos() {
        List<Taco> res = new LinkedList<>();
        res.add(makeTaco("Full fat", "FLTO", "GRBF", "CHED", "JACK", "SRCR"));
        res.add(makeTaco("El veggie", "COTO", "TMTO", "LETC", "SLSA"));
        res.add(makeTaco("Just Lettuce", "FLTO", "LETC", "SRCR"));
        res.add(makeTaco("Leon the Professional",
                IngredientRelation.relation
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
    public String showDesignForm() {
        log.info(">>> {}.showDesignForm()", Helpers.makeLocator(this));
        return "design";
    }

    // ----------------------
    // From Listing 2.6.
    // This is eventually called when the client POSTs to "/design" URL.
    // The "tacoOrder" is labeled as "ModelAttribute" so comes from the "session model".
    // The "taco" is the information that was POSTed, already suitable marshalled into a "Taco" instance. Nice!
    // Can "taco" or "tacoOrder" be null? Could the mutable bean "taco" be invalid? The latter is quite possible!
    // ----------------------

    @PostMapping
    public String processTaco(@NotNull Taco taco, @ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info(">>> {}.processTaco()", Helpers.makeLocator(this));
        log.info(">>>>>> called with {}",Helpers.makeLocator(taco));
        log.info(">>>>>> called with {}",Helpers.makeLocator(tacoOrder));
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
