package tacos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ---
// "No Rights Reserved"
// This code snippet is under
// https://creativecommons.org/share-your-work/public-domain/cc0/
// ---

// ---
// A custom validator for credit card expiration date (or expiry date, same thing)
// ---

// Hints:
//
// https://www.baeldung.com/spring-mvc-custom-validator
// https://blog.payara.fish/getting-started-with-jakarta-ee-9-jakarta-validation
// https://stackoverflow.com/questions/19825563/custom-validator-message-throwing-exception-in-implementation-of-constraintvali

@Slf4j
public class CreditCardExpiryDateValidator implements ConstraintValidator<CreditCardExpiryDate, String> {

    public record AnalysisResult(YearMonth ym, String errorMessage) {

        AnalysisResult(@NotNull YearMonth ym) {
            this(ym, null);
        }

        AnalysisResult(@NotNull String errorMessage) {
            this(null, errorMessage);
        }

        public boolean isError() {
            return errorMessage != null;
        }
    }

    private final static Pattern pat = Pattern.compile("\\s*(\\d+)\\s*/\\s*(\\d+)\\s*");

    @Override
    public void initialize(@NotNull CreditCardExpiryDate constraintAnnotation) {
        // no need to do anything
    }

    @Override
    public boolean isValid(@NotNull String value, @NotNull ConstraintValidatorContext context) {
        AnalysisResult ares = analyze(value);
        if (ares.isError()) {
            // use the bespoke message passed in "ares"
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ares.errorMessage).addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }

    @NotNull
    public static AnalysisResult analyze(@NotNull String value) {
        final Matcher matcher = pat.matcher(value);
        if (!matcher.matches()) {
            return new AnalysisResult("Raw value '" + value + "' does not match regular expression " + pat.pattern());
        }
        final var ccMonth = Integer.valueOf(matcher.group(1));
        final var ccYear = Integer.valueOf(matcher.group(2));
        if (ccMonth < 1 || 12 < ccMonth) {
            return new AnalysisResult("Month is " + ccMonth + ", and thus out of bounds");
        }
        final int ccYear4;
        if (ccYear < 100) {
            // The 2-digit year fudge!!
            // 90...99 --> 1990...1999
            // 00...89 --> 2000...2089
            if (ccYear >= 90) {
                ccYear4 = 1900 + ccYear;
            } else {
                ccYear4 = 2000 + ccYear;
            }
        } else {
            ccYear4 = ccYear;
        }
        // local date in the default time zone
        LocalDate local = LocalDate.now();
        final int curYear = local.get(ChronoField.YEAR);
        final int curMonth = local.get(ChronoField.MONTH_OF_YEAR); // 1..12
        // The validity date may still mean the card has expired if we are at end of month, but hey!
        if (ccYear4 < curYear || (ccYear4 == curYear && ccMonth < curMonth)) {
            return new AnalysisResult(ccMonth + "/" + ccYear4 + " is before now, " + curMonth + "/" + curYear + " - the card has expired");
        }
        return new AnalysisResult(YearMonth.of(ccYear4, ccMonth));
    }

}