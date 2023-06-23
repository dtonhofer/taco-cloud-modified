package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// As usual the visibility of the methods is either "private" (if they are used
// for support) or "default" (if they are annotated with @Test).
// No need to make them "public". Even the class itself can be left at default visibility.

class TestJakartaValidationNotNull extends TestingWithValidator {

    static class Validatable {

        @NotNull // warning: "Not null fields must be initialized"
        private String text;

        public @NotNull String getDisallowedNull() {
            return null; // warning: "'null' is returned by a method declared as @NotNull"
        }

        public void setToDisallowedNull() {
            text = null; // warning: "'null' is assigned to a variable that is annotated with @NotNull"
        }

        public @NotNull String getText() {
            return text;
        }

        public void setText(@NotNull String text) {
            this.text = text;
        }
    }

    @Test
    void violateNotNullJakartaValidationAnnotations() {
        final Validatable validatable = new Validatable();
        assertNull(validatable.getDisallowedNull()); // warning: "the call always fails, according to its method contracts"
        validatable.setToDisallowedNull();
        assertNull(validatable.getText());
        validatable.setText("bar");
        assertEquals("bar", validatable.getText());
        validatable.setText(null);
        assertNull(validatable.getText());
        {
            // just to make sure the violations come in an order that is
            // repeatable when iterating over the container, transform the set into a list
            final List<ConstraintViolation<Validatable>> violations = validateStuff(validatable, Printing.off);
            assertAll(
                    () -> assertEquals(2, violations.size()),
                    () -> assertEquals("must not be null", getMessage(0, violations)),
                    () -> assertEquals("must not be null", getMessage(1, violations)),
                    () -> assertEquals("disallowedNull", getPropertyPath(1, violations)),
                    () -> assertEquals("text", getPropertyPath(0, violations))
            );
        }
    }

}
