package tacos.model.taco;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
@Data
public class TacoOrder {

    @NotBlank(message="Delivery name is required")
    private String deliveryName;

    @NotBlank(message="Street is required")
    private String deliveryStreet;

    @NotBlank(message="City is required")
    private String deliveryCity;

    @NotBlank(message="State is required")
    private String deliveryState;

    @NotBlank(message="Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message="Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer=3, fraction=0, message="Invalid CVV")
    private String ccCVV;

    private Map<String, Taco> tacos = new HashMap<>();

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