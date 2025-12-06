package io.github.dogrulabs.jsonvalidation;

/**
 * Main entry point for JSON validation operations.
 * Supports both syntax checking and typed validation using Bean Validation.
 */
public interface JsonValidator {

    /**
     * Deserializes the JSON into the given type and performs Bean Validation.
     * Use this method when you have a DTO with jakarta.validation constraints.
     *
     * @param json The JSON string to validate.
     * @param type The target class to map the JSON to.
     * @param <T>  The type of the object.
     * @return A ValidationResult containing any syntax, mapping, or constraint
     *         errors.
     * @throws IllegalArgumentException if type is null.
     */
    <T> ValidationResult validate(String json, Class<T> type);

    /**
     * Performs only a JSON syntax check.
     * Does NOT perform DTO mapping or Bean Validation.
     *
     * @param json The JSON string to check.
     * @return A ValidationResult (valid if syntax is correct, invalid otherwise).
     */
    ValidationResult validateSyntax(String json);
}
