package tacos.model.helpers;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// ---
// "No Rights Reserved"
// This code snippet is under
// https://creativecommons.org/share-your-work/public-domain/cc0/
// ---

// ---
// Printing Spring Errors structure
// ---

public class ErrorPrinter {

    // The returned string has no final NL for proper printing in the caller

    public static @NotNull String printErrors(@NotNull Errors errors, int indentCount) {
        StringBuilder buf = new StringBuilder();
        {
            buf.append(String.format("Errors for object '%s' at path '%s': %d global errors, %d field errors, %d errors in total",
                    errors.getObjectName(),
                    errors.getNestedPath(),
                    errors.getGlobalErrorCount(),
                    errors.getFieldErrorCount(),
                    errors.getErrorCount()));
        }
        // "global errors" are object level errors
        if (errors.hasGlobalErrors()) {
            buf.append("\n");
            buf.append("Global Errors");
            buf.append(indent(handleAllGlobalErrors(errors, indentCount), indentCount));
        }
        // "field errors" (those are a specialization of "ObjectError")
        if (errors.hasFieldErrors()) {
            buf.append("\n");
            buf.append("Global Errors");
            buf.append(indent(handleAllFieldErrors(errors, indentCount), indentCount));
        }
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // The returned string has no final NL for proper printing in the caller

    private static @NotNull String handleAllGlobalErrors(@NotNull Errors errors, int indentCount) {
        StringBuilder buf = new StringBuilder();
        for (ObjectError globalError : errors.getGlobalErrors()) {
            buf.append("\n");
            buf.append(handleObjectError(globalError, indentCount));
        }
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // The returned string has no final NL for proper printing in the caller

    private static @NotNull String handleAllFieldErrors(@NotNull Errors errors, int indentCount) {
        StringBuilder buf = new StringBuilder();
        for (FieldError fieldError : errors.getFieldErrors()) {
            buf.append("\n");
            buf.append(handleFieldError(fieldError, indentCount));
        }
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/ObjectError.html
    // The returned string has no final NL for proper printing in the caller

    private static @NotNull String handleObjectError(@NotNull ObjectError objError, int indentCount) {
        StringBuilder buf = new StringBuilder();
        buf.append("ObjectError\n");
        buf.append(indent(handleObjectErrorContent(objError, indentCount), indentCount));
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // FieldError is a specialization of ObjectError
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/FieldError.html
    // The returned string has no final NL for proper printing in the caller

    private static @NotNull String handleFieldError(@NotNull FieldError fieldError, int indentCount) {
        StringBuilder buf = new StringBuilder();
        buf.append("FieldError on field ");
        buf.append("'");
        buf.append(fieldError.getField());
        buf.append("'");
        if (fieldError.isBindingFailure()) {
            buf.append(": binding failure");
        } else {
            buf.append(": validation failure");
        }
        buf.append("\n");
        buf.append(indent(handleObjectErrorContent(fieldError, indentCount), indentCount));
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // The returned string has no final NL for proper printing in the caller

    private static @NotNull String handleObjectErrorContent(@NotNull ObjectError objError, int indentCount) {
        StringBuilder buf = new StringBuilder();
        buf.append("Object name: ");
        buf.append("'");
        buf.append(objError.getObjectName());
        buf.append("'");
        buf.append("\n");
        buf.append("Description:");
        buf.append("\n");
        // buf.append(objError); // a very long line
        // TODO: This can be made more readable!!
        buf.append(indent(recut(objError.toString()), indentCount));
        {
            final @NotNull Object wrapped = objError.unwrap(Object.class);
            {
                buf.append("\n");
                buf.append("Wraps: ");
                buf.append("'");
                buf.append(wrapped.getClass().getName());
                buf.append("'");
            }
        }
        assert notEndsInNewline(buf);
        return buf.toString();
    }

    // My own indent, which unlike String.indent() does NOT add a NL to the last line if there isn't one
    // Why!!

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

    // A primitive way of splitting a very long line at interesting locations

    private final static int maxLength = 70;

    private final static Pattern splitter = Pattern.compile("(.*?)([\\n,;.\\s ])");

    public static String recut(@NotNull String x) {
        final String trimmedX = x.trim();
        final List<String> substrings = new LinkedList<>();
        {
            int extractedCharCount = 0;
            Matcher m = splitter.matcher(trimmedX);
            while (m.find()) {
                final String content = m.group(1);
                final String splittingChar = m.group(2);
                substrings.add(content);
                substrings.add(splittingChar);
                extractedCharCount = extractedCharCount + content.length() + splittingChar.length();
            }
            final String rest = trimmedX.substring(extractedCharCount);
            if (!rest.isEmpty()) {
                substrings.add(rest);
            }
        }
        final List<String> lines = new LinkedList<>();
        {
            StringBuilder line = new StringBuilder();
            for (String substring : substrings) {
                if (substring.equals("\n")) {
                    // next line
                    lines.add(line.toString());
                    line = new StringBuilder();
                } else {
                    // next line only if we are above max length
                    if (line.length() + substring.length() > maxLength) {
                        lines.add(line.toString());
                        line = new StringBuilder(substring);
                    } else {
                        line.append(substring);
                    }
                }
            }
            if (!line.isEmpty()) {
                lines.add(line.toString());
            }
        }
        final String res = String.join("\n", lines);
        assert notEndsInNewline(res);
        return res;
    }
}
