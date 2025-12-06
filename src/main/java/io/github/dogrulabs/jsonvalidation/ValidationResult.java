package io.github.dogrulabs.jsonvalidation;

import java.util.List;

/**
 * Represents the outcome of a validation operation.
 * If valid is true, the errors list is empty.
 * If valid is false, the errors list contains at least one ValidationError.
 *
 * @param valid  True if the JSON/Object is valid, false otherwise.
 * @param errors A list of validation errors. Never null.
 */
public record ValidationResult(
        boolean valid,
        List<ValidationError> errors) {

    /**
     * Creates a successful validation result.
     */
    public static ValidationResult success() {
        return new ValidationResult(true, List.of());
    }

    /**
     * Creates a failed validation result with a single generic error (usually for
     * syntax/mapping errors).
     * Path defaults to "$".
     */
    public static ValidationResult invalid(String message) {
        return new ValidationResult(false,
                List.of(new ValidationError("$", message, null)));
    }

    /**
     * Creates a failed validation result with a list of errors.
     */
    public static ValidationResult invalid(List<ValidationError> errors) {
        return new ValidationResult(false, List.copyOf(errors));
    }
}
