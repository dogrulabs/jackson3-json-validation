# Getting Started

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.dogrulabs</groupId>
    <artifactId>jackson3-json-validation</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add this to your `build.gradle` dependencies:

```groovy
implementation 'io.github.dogrulabs:jackson3-json-validation:1.0.0'
```

## Basic Usage

### 1. Define your Data Transfer Object (DTO)

Annotate your Java class with standard **Jakarta Bean Validation** annotations (e.g., `@NotNull`, `@Size`, `@Email`). This class acts as your schema.

```java
import jakarta.validation.constraints.*;

public class UserDto {
    @NotNull(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 18, message = "User must be at least 18 years old")
    private int age;

    // Standard Getters and Setters (or use Lombok)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

### 2. Create a Validator

The easiest way to get started is using the default validator:

```java
import io.github.dogrulabs.jsonvalidation.JsonValidator;
import io.github.dogrulabs.jsonvalidation.JsonValidators;

JsonValidator validator = JsonValidators.defaultValidator();
```

### 3. Validate JSON

Pass your JSON string and the DTO class to the `validate` method.

```java
String jsonInput = """
    {
        "username": "jd", 
        "email": "not-an-email", 
        "age": 16
    }
""";

ValidationResult result = validator.validate(jsonInput, UserDto.class);
```

### 4. Check Results

The `ValidationResult` object tells you if the JSON is valid and provides a list of errors if it's not.

```java
if (result.valid()) {
    System.out.println("✅ JSON is valid!");
} else {
    System.out.println("❌ JSON is invalid. Errors:");
    
    for (ValidationError error : result.errors()) {
        System.out.println("Path: " + error.path());
        System.out.println("Message: " + error.message());
        System.out.println("Value: " + error.rejectedValue());
        System.out.println("---");
    }
}
```

**Output for the example above:**

```text
❌ JSON is invalid. Errors:
Path: username
Message: Username must be between 3 and 20 characters
Value: jd
---
Path: email
Message: Invalid email format
Value: not-an-email
---
Path: age
Message: User must be at least 18 years old
Value: 16
---
```
