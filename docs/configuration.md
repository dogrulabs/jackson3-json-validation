# Configuration

You can fully customize how both JSON parsing and Bean Validation behave.

## Customizing Jackson (JsonMapper)

You might want to configure Jackson to handle snake_case, ignore unknown properties, or support Java 8 dates.

```java
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.DeserializationFeature;

JsonMapper mapper = JsonMapper.builder()
    .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .build();

// Use the custom mapper
JsonValidator validator = JsonValidators.jacksonOnly(mapper);
// OR for full validation:
// JsonValidator validator = JsonValidators.custom(mapper, someValidator);
```

## Customizing Bean Validation (Validator)

You can provide a custom `jakarta.validation.Validator` instance. For example, you might want to enable "Fail Fast" mode (stop at the first error).

```java
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

Validator validator = Validation.byProvider(HibernateValidator.class)
    .configure()
    .failFast(true) // Return only the first error found
    .buildValidatorFactory()
    .getValidator();
```

## Creating a Custom JsonValidator

Combine your custom `JsonMapper` and `Validator` using the factory method:

```java
JsonValidator customValidator = JsonValidators.custom(mapper, validator);
```
