# Error Handling

The `validate` method returns a `ValidationResult` object. It is designed to be safe and easy to inspect.

## ValidationResult

- `valid()`: Boolean. Returns `true` if there were no errors.
- `errors()`: Returns a `List<ValidationError>`. It is never null. If valid, it is empty.

## ValidationError Structure

Each `ValidationError` contains:

- `path()`: A string representation of the path to the invalid field (e.g., `user.address.zipCode`).
    - For top-level fields: `fieldName`
    - For nested fields: `parent.child`
    - For lists: `items[0].id`
- `message()`: The error message (e.g., "must not be null").
- `rejectedValue()`: The value that caused the error (nullable).

## Example Pattern

```java
ValidationResult result = validator.validate(json, MyDto.class);

if (!result.valid()) {
    // 1. Log errors
    result.errors().forEach(e -> logger.warn("Validation failed at {}: {}", e.path(), e.message()));

    // 2. Throw an exception (if using in a web framework)
    throw new MyValidationException(result.errors());
    
    // 3. Or return a specific error response
    return ResponseEntity.badRequest().body(result.errors());
}
```

## Handling Syntax Errors

If the input JSON is malformed (e.g., missing brackets), the validator handles this gracefully.
It will return a `ValidationResult` with `valid() == false` and a single `ValidationError` where:
- `path()` is usually `$` or the path where parsing failed.
- `message()` contains the raw Jackson exception message (e.g., "Unexpected character...").
