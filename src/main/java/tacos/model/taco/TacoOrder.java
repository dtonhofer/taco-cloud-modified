package tacos.model.taco;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.jetbrains.annotations.NotNull;
import tacos.model.helpers.Helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

// ---
// Based on Listing 2.3 of "Spring in Action" 6th edition
// ---

// ---
// This is a mutable Java Bean from which the Spring framework reads values and into which it
// inserts values. Getters and setters are created by Lombok via the @Data annotation.
// ---


@Slf4j
@Data
public class TacoOrder {

    public TacoOrder() {
        log.info(">>> {} created", Helpers.makeLocator(this));
    }

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    // TODO: Book has a superfluous pair of backslash here
    // TODO: "regex" is not the appropriate tool to check a date
    @Pattern(regexp = "^(0[1-9]|1[0-2])([/])([2-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    private Map<String, Taco> tacos = new HashMap<>();

    // We are rather severe in accepting Tacos!
    // An exception here will result in an "Internal Server Error", not very nice.

    public void addTaco(@NotNull Taco taco) {
        if (taco.getName() == null) {
            throw new IllegalArgumentException("The passed taco has a null name");
        }
        String goodName = taco.getName().trim();
        if ("".equals(goodName)) {
            throw new IllegalArgumentException("The passed taco has no name");
        }
        if (taco.getIngredients().isEmpty()) {
            throw new IllegalArgumentException("The passed taco has no ingredients");
        }
        if (tacos.containsKey(goodName)) {
            throw new IllegalArgumentException("There already is a taco named " + goodName);
        }
        this.tacos.put(goodName, taco);
    }

    public SortedSet<String> getTacoNames() {
        return new TreeSet<>(tacos.keySet());
    }
}