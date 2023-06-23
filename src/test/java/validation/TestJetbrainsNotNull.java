package validation;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// As usual the visibility of the methods is either "private" (if they are used
// for support) or "default" (if they are annotated with @Test).
// No need to make them "public". Even the class itself can be left at default visibility.

class TestJetbrainsNotNull {

    static class Validatable {

        @NotNull // warning: "Not null fields must be initialzed"
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
    void violateNotNullJetBrainsAnnotations() {
        final Validatable validatable = new Validatable();
        assertNull(validatable.getDisallowedNull()); // warning: "the call always fails, according to its method contracts"
        validatable.setToDisallowedNull();
        assertNull(validatable.getText());
        validatable.setText("bar");
        assertEquals("bar", validatable.getText());
        validatable.setText(null);
        assertNull(validatable.getText());
    }

}
