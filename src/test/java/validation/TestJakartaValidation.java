package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// As usual the visibility of the methods is either "private" (if they are used
// for support) or "default" (if they are annotated with @Test).
// No need to make them "public". Even the class itself can be left at default visibility.

class TestJakartaValidation extends TestingWithValidator {

    @Data
    static class StringContainer {

        // @NotEmpty: "Checks whether the annotated element is not null nor empty"

        @NotEmpty
        private String notEmpty;

        // @NotBlank: "Checks that the annotated character sequence is not null and the trimmed length is greater than 0."

        @NotBlank
        private String notBlank;

    }

    @Test
    void noViolations() {
        final StringContainer sc = new StringContainer();
        sc.setNotEmpty("   "); // is indeed not empty
        sc.setNotBlank("aaa"); // is indeed not blank
        final List<ConstraintViolation<StringContainer>> violations = validateStuff(sc, Printing.off);
        assertTrue(violations.isEmpty());
    }

    @Test
    void violateNotEmpty1() {
        final StringContainer sc = new StringContainer();
        sc.setNotEmpty(""); // is empty, violating not-empty constraints
        sc.setNotBlank("aaa"); // is indeed not blank
        final List<ConstraintViolation<StringContainer>> violations = validateStuff(sc, Printing.off);
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("must not be empty", getMessage(0, violations)),
                () -> assertEquals("notEmpty", getPropertyPath(0, violations))
        );
    }

    @Test
    void violateNotEmpty2() {
        final StringContainer sc = new StringContainer();
        sc.setNotEmpty(null); // is null, violating not-empty constraints
        sc.setNotBlank("aaa"); // is indeed not blank
        final List<ConstraintViolation<StringContainer>> violations = validateStuff(sc, Printing.off);
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("must not be empty", getMessage(0, violations)),
                () -> assertEquals("notEmpty", getPropertyPath(0, violations))
        );
    }

    @Test
    void violateNotBlank() {
        final StringContainer sc = new StringContainer();
        sc.setNotEmpty("xxx"); // definitely not empty
        sc.setNotBlank(" "); // blank, violating not-blank constraints
        final List<ConstraintViolation<StringContainer>> violations = validateStuff(sc, Printing.off);
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("must not be blank", getMessage(0, violations)),
                () -> assertEquals("notBlank", getPropertyPath(0, violations))
        );
    }

}
