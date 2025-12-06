package io.github.dogrulabs.jsonvalidation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * Implementation of JsonValidator using Jackson 3 for parsing/mapping
 * and Jakarta Validator for bean validation.
 * This class is immutable and thread-safe.
 */
public final class Jackson3JsonValidator implements JsonValidator {

    private final JsonMapper mapper;
    private final Validator beanValidator;

    /**
     * Constructs a validator.
     *
     * @param mapper        Jackson 3 JsonMapper (must not be null).
     * @param beanValidator Jakarta Validator (nullable; if null, bean validation is
     *                      skipped).
     */
    public Jackson3JsonValidator(JsonMapper mapper, Validator beanValidator) {
        if (mapper == null) {
            throw new IllegalArgumentException("JsonMapper must not be null");
        }
        this.mapper = mapper;
        this.beanValidator = beanValidator;
    }

    @Override
    public <T> ValidationResult validate(String json, Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Target type must not be null");
        }
        if (json == null || json.isBlank()) {
            return ValidationResult.invalid("JSON body must not be null or blank");
        }

        try {
            // 1) Parse + mapping
            T value = mapper.readValue(json, type);

            // 2) Bean Validation (optional)
            if (beanValidator == null) {
                return ValidationResult.success();
            }

            List<ValidationError> violations = beanValidator
                    .validate(value)
                    .stream()
                    .map(this::toValidationError)
                    .toList();

            if (!violations.isEmpty()) {
                return ValidationResult.invalid(violations);
            }

            return ValidationResult.success();

        } catch (StreamReadException e) {
            // JSON syntax errors
            return ValidationResult.invalid("Invalid JSON syntax: " + safeMessage(e));
        } catch (DatabindException e) {
            // JSON structure does not match target (e.g. type mismatch)
            return ValidationResult.invalid("JSON structure does not match target type: " + safeMessage(e));
        }
    }

    @Override
    public ValidationResult validateSyntax(String json) {
        if (json == null || json.isBlank()) {
            return ValidationResult.invalid("JSON body must not be null or blank");
        }
        try {
            mapper.readTree(json); // Just parsing
            return ValidationResult.success();
        } catch (StreamReadException e) {
            return ValidationResult.invalid("Invalid JSON syntax: " + safeMessage(e));
        }
    }

    private ValidationError toValidationError(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath() != null
                ? violation.getPropertyPath().toString()
                : "$";

        String message = violation.getMessage() != null
                ? violation.getMessage()
                : "Validation failed";

        Object invalidValue = violation.getInvalidValue();

        return new ValidationError(path, message, invalidValue);
    }

    private String safeMessage(Throwable t) {
        String msg = t.getMessage();
        return msg != null ? msg : t.getClass().getSimpleName();
    }
}
