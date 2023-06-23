package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// All the methods can be declared with "default visibility"
// and actually be declared "protected"

@Slf4j
public abstract class TestingWithValidator {

    protected static Validator validator;

    enum Printing {on, off}

    // A method executed by the JUnit framework before
    // any of the tests proper are run.

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // instead of a set, return a list to ensure a consistent order in
    // iterations made at different times (the order may be consistent
    // with the Set<> but can we be sure?)

    static <T> List<ConstraintViolation<T>> validateStuff(@NotNull T v, Printing printing) {
        Set<ConstraintViolation<T>> violations = validator.validate(v);
        if (Printing.on == printing) {
            if (!violations.isEmpty()) {
                log.info("Found {} violations", violations.size());
                violations.forEach(violation -> log.info(violation.toString()));
            } else {
                log.info("No violations");
            }
        }
        // TODO: This may not be good enough; one would have to create a special class with
        // TODO: the values from "violations" one is interested in, define an order (and equality)
        // TODO: and sort it oneself!
        return new ArrayList<>(violations);
    }

    static <T> String getMessage(int n, List<ConstraintViolation<T>> violations) {
        ConstraintViolation<T> found = violations.stream().skip(n).findFirst().orElseThrow();
        return found.getMessage();
    }

    static <T> String getPropertyPath(int n, List<ConstraintViolation<T>> violations) {
        ConstraintViolation<T> found = violations.stream().skip(n).findFirst().orElseThrow();
        return found.getPropertyPath().toString();
    }

}
