package tacos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Explainer at https://blog.payara.fish/getting-started-with-jakarta-ee-9-jakarta-validation

@Retention(RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Documented
@Constraint(validatedBy = CreditCardExpiryDateValidator.class)
public @interface CreditCardExpiryDate {

    String message(); // no default

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
