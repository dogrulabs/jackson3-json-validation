package io.github.dogrulabs.jsonvalidation;

import io.github.dogrulabs.jsonvalidation.example.ComplexPojo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexValidatorTest {

    private final JsonValidator validator = JsonValidators.defaultValidator();

    @Test
    void validate_shouldReturnValid_forFullyPopulatedCorrectJson() {
        String json = """
                {
                  "name": "John Doe",
                  "age": 30,
                  "email": "john.doe@example.com",
                  "tags": ["developer", "java"],
                  "address": {
                    "city": "Istanbul",
                    "zipCode": "34000"
                  },
                  "scores": {
                    "math": 95,
                    "history": 80
                  }
                }
                """;
        ValidationResult result = validator.validate(json, ComplexPojo.class);
        assertTrue(result.valid(), "Should be valid: " + result.errors());
    }

    @Test
    void validate_shouldReturnInvalid_whenMultipleConstraintsViolated() {
        String json = """
                {
                  "name": "",
                  "age": 10,
                  "email": "not-an-email",
                  "tags": [],
                  "address": {
                    "city": null,
                    "zipCode": "123"
                  }
                }
                """;
        ValidationResult result = validator.validate(json, ComplexPojo.class);
        assertFalse(result.valid());

        // Asserting specific errors based on what we expect
        // name: blank
        // age: < 18
        // email: invalid
        // tags: size < 1
        // address.city: null
        // address.zipCode: pattern mismatch
        assertEquals(6, result.errors().size(), "Should have exactly 6 errors");
    }

    @Test
    void validate_shouldReturnInvalid_whenNestedMapValueInvalid() {
        String json = """
                {
                  "name": "Jane",
                  "age": 25,
                  "scores": {
                    "math": 0
                  }
                }
                """;
        // math score 0 violates @Min(1)
        ValidationResult result = validator.validate(json, ComplexPojo.class);
        assertFalse(result.valid());
        assertTrue(result.errors().stream()
                .anyMatch(e -> e.path().contains("scores[math]") || e.path().contains("scores['math']")),
                "Error path should indicate the specific map key");
    }
}
