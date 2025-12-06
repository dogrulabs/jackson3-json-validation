package io.github.dogrulabs.jsonvalidation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import tools.jackson.databind.json.JsonMapper;

/**
 * Factory class for creating JsonValidator instances.
 */
public final class JsonValidators {

    private JsonValidators() {
        // utility class
    }

    /**
     * Creates a full-featured default validator with:
     * - Default Jackson 3 JsonMapper
     * - Default Hibernate Validator (Bean Validation)
     */
    public static JsonValidator defaultValidator() {
        JsonMapper mapper = JsonMapper.builder().build();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return new Jackson3JsonValidator(mapper, validator);
    }

    /**
     * Creates a validator that performs only Jackson 3 syntax and mapping checks.
     * Bean Validation is disabled.
     *
     * @param mapper The JsonMapper to use (must not be null).
     */
    public static JsonValidator jacksonOnly(JsonMapper mapper) {
        return new Jackson3JsonValidator(mapper, null);
    }

    /**
     * Creates a custom validator with user-provided components.
     *
     * @param mapper    The JsonMapper to use (must not be null).
     * @param validator The Jakarta Validator to use (nullable).
     */
    public static JsonValidator custom(JsonMapper mapper, Validator validator) {
        return new Jackson3JsonValidator(mapper, validator);
    }
}
