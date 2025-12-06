package io.github.dogrulabs.jsonvalidation;

/**
 * Represents a single validation error found during processing.
 *
 * @param path          The property path of the error (e.g., "attributes.color"
 *                      or "$").
 * @param message       A human-readable error message in English.
 * @param rejectedValue The value that failed validation (may be null).
 */
public record ValidationError(
                String path,
                String message,
                Object rejectedValue) {
}
