package tacos.model.ingredients;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// ---
// This is wrapper around String, basically giving us a String with a specific type.
// This helps enormously when writing code and looking for problems (the alternative
// is to use String directly - and every String looks like s String, leading to various
// confusions).
// ---

public final class IngredientId {

    // We demand that the String encapsulated by IngredientId is not empty, trimmed and uppercase.
    // Thus casing is irrelevant for ids. This is enforced in the constructor.

    private final @NotNull String id;

    // The constructor doesn't care about the casing of the string "id"
    // and forces it to uppercase.

    public IngredientId(@NotNull String id) {
        String tid = id.trim().toUpperCase();
        if ("".equals(tid)) {
            throw new IllegalArgumentException("Empty string!");
        }
        this.id = tid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientId that = (IngredientId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        assert id.hashCode() == Objects.hashCode(id);
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IngredientId-" + id;
    }

    // For Thymeleaf templates, we need the raw string underlying the id.
    // No fancy formatting provided by ".toString()"
    // The returned string is all uppercase.

    public String getRaw() {
        return id;
    }
}
