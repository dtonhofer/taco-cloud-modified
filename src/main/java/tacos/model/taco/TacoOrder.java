package tacos.model.taco;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.jetbrains.annotations.NotNull;
import tacos.model.helpers.Helpers;
import tacos.validation.CreditCardExpiryDate;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

// ---
// Based on Listing 2.3 of "Spring in Action" 6th edition
// ---

// ---
// This is a mutable Java Bean from/into which the Spring framework sets/gets values.
// Getters and setters are created by Lombok via the @Data annotation.
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

    @CreditCardExpiryDate(message = "Bad credit card expiry date")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    private Map<String, Taco> tacos = new HashMap<>();

    // We are rather severe in accepting Tacos!
    // An exception here will result in an "Internal Server Error", not very nice.
    // Validation has checked that the Taco has valid content (good name, actual
    // ingredients), so no need to do additional checks here.
    // TODO: Check that a Taco with the given name does not exist at Taco naming time

    public void addTaco(@NotNull Taco taco) {
        if (tacos.containsKey(taco.getName())) {
            // This cannot have been caught by validation...
            throw new IllegalArgumentException("There already is a taco named " + taco.getName());
        }
        this.tacos.put(taco.getName(), taco);
    }

    public SortedSet<String> getTacoNames() {
        return new TreeSet<>(tacos.keySet());
    }
}