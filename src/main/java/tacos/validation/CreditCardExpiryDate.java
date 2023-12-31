package tacos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

// ---
// "No Rights Reserved"
// This code snippet is under
// https://creativecommons.org/share-your-work/public-domain/cc0/
// ---

// ---
// A special "Jakarta Bean Validation" annotation to check credit card expiry date
// ---

@Retention(RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Documented
@Constraint(validatedBy = CreditCardExpiryDateValidator.class)
public @interface CreditCardExpiryDate {

    String message() default "Bad expiry date"; // the message is actually created by the "validator"

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
