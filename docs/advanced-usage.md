# Advanced Usage

## Nested Objects

To validate nested objects, use the standard `@Valid` annotation on the field. This triggers validation cascading.

```java
public class AddressDto {
    @NotNull
    private String street;
    
    @NotNull
    private String city;
}

public class UserDto {
    @NotNull
    private String name;

    @NotNull
    @Valid // <--- Crucial for cascading validation
    private AddressDto address;
}
```

## Collections (Lists & Maps)

The `@Valid` annotation also works for Collections.

```java
public class OrderDto {
    @NotNull
    private String orderId;

    @NotNull
    @Size(min = 1)
    private List<@Valid OrderItemDto> items; // Validate each item in the list
}
```

*Note: Depending on your Java version and Bean Validation provider, you might need to place `@Valid` on the type argument (as shown above) or on the field.*

## Custom Constraints

You can create your own custom validation annotations.

### 1. Define the Annotation

```java
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = IsoCountryCodeValidator.class)
public @interface IsoCountryCode {
    String message() default "Invalid ISO Country Code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### 2. Implement the Validator

```java
public class IsoCountryCodeValidator implements ConstraintValidator<IsoCountryCode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // Let @NotNull handle nulls
        return value.length() == 2 && value.matches("[A-Z]+");
    }
}
```

### 3. Use it

```java
public class AddressDto {
    @IsoCountryCode
    private String country;
}
```

## Syntax-Only Validation

If you don't have a DTO or just want to check if a string is well-formed JSON without binding it to a class:

```java
ValidationResult result = validator.validateSyntax("{ broken: json ");

if (!result.valid()) {
    // result.errors() will contain the syntax error details from Jackson
    System.out.println("Syntax Error: " + result.errors().get(0).message());
}
```
