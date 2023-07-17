package tacos.model.taco;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

// ---
// Based on Listing 2.3 of "Spring in Action" 6th edition
// ---

// ---
// This is a mutable Java Bean from/into which the Spring framework sets/gets values.
// Getters and setters are created by Lombok via the @Data annotation.
// ---

@Slf4j
@Data
public class TacoOrder implements Serializable {

    // Added in Chapter 3.1.1
    private static final long serialVersionUID = 1L;

    // Added in Chapter 3.1.1
    private Long id;

    // Added in Chapter 3.1.1
    private Date placedAt;

    private Map<String, Taco> tacos = new HashMap<>();

    // Is it right to create instance of CreditCardInfo and DeliveryAddress here?
    // Should Spring Framework take over the wiring?
    // Can these be final (probably not if the framework expects them to be settable)

    @Valid
    private CreditCardInfo ccInfo = new CreditCardInfo();

    @Valid
    private Address deliveryAddress = new Address();

    // We are rather severe in accepting Tacos!
    // An exception here will result in an "Internal Server Error", not very nice.
    // Validation has checked that the Taco has valid content (good name, actual
    // ingredients), so no need to do additional checks here.
    // TODO: Check that a Taco with the given name does not yet exist at Taco naming time
    // Possibilities:
    // - Throw an exception in the controller handler and have something in spring that handles it

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