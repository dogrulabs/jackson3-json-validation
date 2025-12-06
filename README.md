# Jackson 3 JSON Validation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dogrulabs/jackson3-json-validation?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.dogrulabs/jackson3-json-validation)
[![Build Status](https://img.shields.io/github/actions/workflow/status/dogrulabs/jackson3-json-validation/ci.yml?branch=main)](https://github.com/dogrulabs/jackson3-json-validation/actions)
[![License](https://img.shields.io/github/license/dogrulabs/jackson3-json-validation)](LICENSE)

A lightweight Java library for validating JSON against Java DTOs using **Jackson 3** and **Jakarta Bean Validation**.

It avoids JSON Schema complexity by treating your Java classes (DTOs) as the schema definition.

## Features

- **Java 17** baseline.
- **Jackson 3** (`tools.jackson.*`) for JSON parsing and binding.
- **Jakarta Bean Validation 3.0+** for constraints (e.g. `@NotNull`, `@Size`).
- **Zero Boilerplate**: No manual schema definition required.
- **Simple API**: Functional API returning a clear `ValidationResult`.
- **Lightweight**: No heavy frameworks (Spring, etc.) required.

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.dogrulabs</groupId>
    <artifactId>jackson3-json-validation</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### 1. Define your DTOs

Use standard Jakarta Validation annotations:

```java
import jakarta.validation.constraints.*;
// ... package definition

public class UserDto {
    @NotNull
    private String username;

    @Email
    private String email;

    @Min(18)
    private int age;
    
    // getters setters
}
```

### 2. Validate JSON

```java
import io.github.dogrulabs.jsonvalidation.JsonValidator;
import io.github.dogrulabs.jsonvalidation.JsonValidators;
import io.github.dogrulabs.jsonvalidation.ValidationResult;

public class Main {
    public static void main(String[] args) {
        // Create default validator
        JsonValidator validator = JsonValidators.defaultValidator();

        String json = """
            {
                "username": "user1",
                "email": "invalid-email",
                "age": 10
            }
        """;

        // Validate
        ValidationResult result = validator.validate(json, UserDto.class);

        if (!result.valid()) {
            System.out.println("JSON is invalid:");
            result.errors().forEach(err -> 
                System.out.printf("- Field: %s, Message: %s (Value: %s)%n", 
                    err.path(), err.message(), err.rejectedValue())
            );
        } else {
            System.out.println("JSON is valid!");
        }
    }
}
```

### 3. Syntax Check Only

If you only want to check if the string is valid JSON (well-formed):

```java
ValidationResult result = validator.validateSyntax("{ broken: json ");
// result.valid() -> false
// result.errors() -> ["Invalid JSON syntax: ..."]
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[Apache License 2.0](LICENSE)
