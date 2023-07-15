package tacos.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tacos.model.helpers.ErrorPrinter;
import tacos.model.helpers.Helpers;
import tacos.model.taco.TacoOrder;

// ---
// Based on Listing 2.8 of "Spring in Action" 6th edition
// ---

@Slf4j
@Controller // Spring will create an instance of this class in the Spring application context at scan time
@RequestMapping("/orders") // Kind of requests that this controller handles
@SessionAttributes("tacoOrder") // The bean stored under "tacoOrder" has session scope (is retained between requests)
public class OrderController {

    public OrderController() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    // Obtain a new, empty TacoOrder instance for insertion into the (session-scoped) model
    // This seems to never be called.
    // But is needed to the tell the IDE that
    // th:object="${tacoOrder}" in "orderForm.html" refers a TacoOrder instance!

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        TacoOrder res = new TacoOrder();
        log.info(">>> {}.order(): new empty {} created",
                Helpers.makeLocator(this),
                Helpers.makeLocator(res));
        return res;
    }

    // This method just because we want to see what happens if we have it

    @ModelAttribute
    public void dummyCall(@NotNull Model model) {
        log.info(">>> {}.dummyCall() called with Model {}",
                Helpers.makeLocator(this),
                Helpers.makeLocator(model));
    }

    // ------------------------------------------------------------
    // Request handling below. The initial path has been given by the class annotation @RequestMapping
    // ------------------------------------------------------------

    // "Errors" must be declared immediately after the "TacoOrder" or you
    // will get an internal server error.

    // Listing 2.10
    @PostMapping
    public String processOrder(@NotNull @Valid TacoOrder tacoOrder, @NotNull Errors errors, @NotNull SessionStatus sessionStatus) {
        log.info(">>> {}.processOrder()", Helpers.makeLocator(this));
        log.info(">>>>>> 'tacoOrder' argument is {}", Helpers.makeLocator(tacoOrder));
        final int indentCount = 3;
        if (errors.hasErrors()) {
            log.info(">>>>>> 'errors' argument is {}", Helpers.makeLocator(errors));
            log.info(">>>>>> errors detail");
            String err = ErrorPrinter.printErrors(errors, indentCount);
            log.info(Helpers.indent(err, indentCount));
        }
        if (errors.hasErrors()) {
            // Just redisplay the order form with the TacoOrder already set up.
            // The form will display all the errors in the "Fields" class available to Thymeleaf.
            return "orderForm";
        } else {
            // Clean up session and be ready for a new order (i.e. the TacoOrder
            // instance is dropped from the session-scoped model)
            sessionStatus.setComplete();
            return "redirect:/";
        }
    }

    // Called when the order form is requested

    @GetMapping("/current")
    public @NotNull String orderForm(@NotNull @ModelAttribute TacoOrder order) {
        log.info(">>> {}.orderForm() called with {}", Helpers.makeLocator(this), Helpers.makeLocator(order));
        return "orderForm";
    }
}
