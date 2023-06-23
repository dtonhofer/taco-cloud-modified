package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tacos.model.taco.Taco;
import tacos.model.taco.TacoOrder;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder") // there is a TacoOrder "session backing bean" in the session model
public class OrderController {

    // Called when the order form is requested

    @GetMapping("/current")
    public String orderForm(@ModelAttribute @NotNull TacoOrder tacoOrder) {
        log.info("OrderController.orderForm() called");
        return "orderForm";
    }

    // Add an initially empty mutable TacoOrder to the SESSION MODEL
    // This seems to be not called but is needed to the tell the IDE that
    // th:object="${tacoOrder}" in "orderForm.html" refers a TacoOrder instance!

    @ModelAttribute(name = "tacoOrder")
    public @NotNull TacoOrder order() {
        log.info("Empty TacoOrder created in OrderController.order()");
        return new TacoOrder();
    }

    // Listing 2.10
    @PostMapping
    public String processOrder(@NotNull TacoOrder order, @NotNull SessionStatus sessionStatus) {
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
