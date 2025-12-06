package io.github.dogrulabs.jsonvalidation;

import io.github.dogrulabs.jsonvalidation.example.MappingDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonValidatorTest {

  private final JsonValidator validator = JsonValidators.defaultValidator();

  // --- validateSyntax Tests ---

  @Test
  void validateSyntax_shouldReturnValid_forCorrectJson() {
    String json = "{\"foo\": \"bar\"}";
    ValidationResult result = validator.validateSyntax(json);
    assertTrue(result.valid());
  }

  @Test
  void validateSyntax_shouldReturnInvalid_forMalformedJson() {
    String json = "{ foo: "; // syntax error
    ValidationResult result = validator.validateSyntax(json);
    assertFalse(result.valid());
    assertFalse(result.errors().isEmpty());
    assertTrue(result.errors().get(0).message().contains("Invalid JSON syntax"));
  }

  @Test
  void validateSyntax_shouldReturnInvalid_forNullOrBlank() {
    assertFalse(validator.validateSyntax(null).valid());
    assertFalse(validator.validateSyntax("").valid());
    assertFalse(validator.validateSyntax("   ").valid());
  }

  // --- validate (DTO) Tests ---

  @Test
  void validate_shouldReturnValid_forValidDto() {
    String json = """
        {
          "mappedAttributes": {
            "a1": {
              "confidence": 0.9,
              "value": "v1"
            }
          }
        }
        """;
    ValidationResult result = validator.validate(json, MappingDocument.class);
    assertTrue(result.valid(), "Should be valid: " + result.errors());
  }

  @Test
  void validate_shouldReturnInvalid_whenBeanConstraintViolated() {
    // confidence > 1.0 (invalid)
    String json = """
        {
          "mappedAttributes": {
            "a1": {
              "confidence": 1.5
            }
          }
        }
        """;
    ValidationResult result = validator.validate(json, MappingDocument.class);
    assertFalse(result.valid());
    assertEquals(1, result.errors().size());
    ValidationError error = result.errors().get(0);
    assertTrue(error.path().contains("confidence"));
  }

  @Test
  void validate_shouldReturnInvalid_whenTypeMismatchOkChecksMapping() {
    // "confidence" expects Double, got String "im_not_a_number"
    String json = """
        {
          "mappedAttributes": {
            "a1": {
              "confidence": "im_not_a_number"
            }
          }
        }
        """;
    ValidationResult result = validator.validate(json, MappingDocument.class);
    assertFalse(result.valid());
    // Should catch DatabindException wrapped in message
    assertTrue(result.errors().get(0).message().contains("JSON structure does not match"));
  }

  @Test
  void validate_shouldSupportNestedValidation() {
    // Conflict inner object has @NotNull sourceId
    String json = """
        {
          "mappedAttributes": {
            "a1": {
              "conflicts": [
                 { "sourceId": null }
              ]
            }
          }
        }
        """;
    ValidationResult result = validator.validate(json, MappingDocument.class);
    assertFalse(result.valid());
    assertTrue(result.errors().stream().anyMatch(e -> e.path().contains("conflicts")));
  }
}
