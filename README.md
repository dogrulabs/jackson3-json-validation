# Jackson 3 JSON Validation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dogrulabs/jackson3-json-validation?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.dogrulabs/jackson3-json-validation)
[![Build Status](https://img.shields.io/github/actions/workflow/status/dogrulabs/jackson3-json-validation/ci.yml?branch=main)](https://github.com/dogrulabs/jackson3-json-validation/actions)
[![License](https://img.shields.io/github/license/dogrulabs/jackson3-json-validation)](LICENSE)

A lightweight Java library for validating JSON against Java DTOs using **Jackson 3** and **Jakarta Bean Validation**.

It avoids JSON Schema complexity by treating your Java classes (DTOs) as the schema definition.

## 📚 Documentation

Detailed documentation is available in the `docs` folder:

- [**Getting Started**](docs/getting-started.md): Installation and basic "Hello World" example.
- [**Advanced Usage**](docs/advanced-usage.md): Nested validation, collections, and custom constraints.
- [**Configuration**](docs/configuration.md): Customizing the Jackson Mapper and Validator.
- [**Error Handling**](docs/error-handling.md): Understanding and using `ValidationResult`.

## 🚀 Quick Start

### 1. Install

```xml
<dependency>
    <groupId>io.github.dogrulabs</groupId>
    <artifactId>jackson3-json-validation</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Validate

```java
// 1. Define DTO
public class UserDto {
    @NotNull
    public String username;
}

// 2. Validate
JsonValidator validator = JsonValidators.defaultValidator();
ValidationResult result = validator.validate("{\"username\": null}", UserDto.class);

if (!result.valid()) {
    System.out.println(result.errors()); // [ValidationError(path=username, message=must not be null)]
}
```

## Features

- **Java 17** baseline.
- **Jackson 3** (`tools.jackson.*`) for JSON parsing and binding.
- **Jakarta Bean Validation 3.0+** for constraints.
- **Zero Boilerplate**: No manual schema definition required.
- **Simple API**: Functional API returning a clear `ValidationResult`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[Apache License 2.0](LICENSE)
