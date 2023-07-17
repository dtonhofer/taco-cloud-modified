package tacos.model.helpers;

import org.jetbrains.annotations.NotNull;
import tacos.model.taco.Taco;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Helpers {

    public final static int usualIndentCount = 3;

    // Make a String to identify a given object. The string will be inserted into logging messages
    // Object may be null.

    public static String makeLocator(Object obj) {
        String res;
        if (obj == null) {
            res = "(null)";
        }
        else {
            res = String.format("%s[0x%xd]", obj.getClass().getName(), System.identityHashCode(obj));
            // FIXME temporary Taco special
            if (obj instanceof Taco) {
                res += " named '" + ((Taco) obj).getName() + "'";
            }
        }
        return res;
    }

    // My own indent, which unlike String.indent() does NOT add a NL to the last line if there isn't one
    // Why does String.indent() do that? Don't know!

    public static @NotNull String indent(@NotNull String text) {
        return indent(text, usualIndentCount);
    }

    public static @NotNull String indent(@NotNull String text, int indentCount) {
        if (indentCount <= 0) {
            return text;
        } else {
            final String prefix = IntStream.range(0, indentCount).mapToObj(x -> " ").collect(Collectors.joining()); // this should be buffered
            final String[] lines = text.split("\n");
            final String res = Arrays.stream(lines).map(line -> prefix + line).collect(Collectors.joining("\n"));
            assert notEndsInNewline(res);
            return res;
        }
    }

    public static boolean notEndsInNewline(@NotNull String x) {
        return x.isEmpty() || !"\n".equals(x.substring(x.length() - 1));
    }

    public static boolean notEndsInNewline(@NotNull StringBuilder x) {
        return x.isEmpty() || !"\n".equals(x.substring(x.length() - 1));
    }
}
