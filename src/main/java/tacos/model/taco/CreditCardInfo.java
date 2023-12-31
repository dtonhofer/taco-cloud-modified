package tacos.model.taco;

import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CreditCardNumber;
import tacos.model.helpers.Helpers;
import tacos.validation.CreditCardExpiryDate;

@Slf4j
@Data
public class CreditCardInfo {

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @CreditCardExpiryDate // message built in validator
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid credit card CVV")
    private String ccCVV;

    // In order to pre-fill form fields, deliver valid data immediately!
    // Data has been generated by a random credit card data generator.

    public CreditCardInfo() {
        log.info(">>> {} created", Helpers.makeLocator(this));
        this.ccNumber = "4772687290188749";
        this.ccExpiration = "05/2026";
        this.ccCVV = "550";
    }
}
