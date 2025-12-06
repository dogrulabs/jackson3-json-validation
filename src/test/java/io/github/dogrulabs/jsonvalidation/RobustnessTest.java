package io.github.dogrulabs.jsonvalidation;

import io.github.dogrulabs.jsonvalidation.example.ComplexPojo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobustnessTest {

    private final JsonValidator validator = JsonValidators.defaultValidator();

    @Test
    void validateSyntax_shouldHandleLargePayload() {
        // Create a large JSON array with 5000 elements
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 5000; i++) {
            sb.append("{\"id\":").append(i).append("}");
            if (i < 4999) {
                sb.append(",");
            }
        }
        sb.append("]");
        String json = sb.toString();

        long start = System.currentTimeMillis();
        ValidationResult result = validator.validateSyntax(json);
        long duration = System.currentTimeMillis() - start;

        assertTrue(result.valid(), "Large payload should be valid syntax");
        System.out.println("Large payload validation took " + duration + "ms");
    }

    @Test
    void validateSyntax_shouldHandleDeepNesting_orFailGracefully() {
        // Create deeply nested JSON: {"a":{"a":{"a":...}}}
        // 500 levels deep
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("{\"a\":");
        }
        sb.append("1");
        for (int i = 0; i < 500; i++) {
            sb.append("}");
        }
        String json = sb.toString();

        ValidationResult result = validator.validateSyntax(json);
        assertTrue(result.valid(), "Should handle 500 levels of nesting without stack overflow");
    }

    @Test
    void validate_shouldHandleTypeMismatchesGracefully() {
        // Pass a string where an integer is expected in ComplexPojo
        String json = """
                {
                  "name": "Test",
                  "age": "invalid_age",
                  "email": "test@test.com",
                  "tags": ["a"]
                }
                """;

        ValidationResult result = validator.validate(json, ComplexPojo.class);
        assertFalse(result.valid());
        // Should capture Jackson deserialization error
        assertTrue(result.errors().get(0).message().contains("JSON structure does not match"));
    }
}
