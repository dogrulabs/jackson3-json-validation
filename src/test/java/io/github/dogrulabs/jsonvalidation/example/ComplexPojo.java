package io.github.dogrulabs.jsonvalidation.example;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class ComplexPojo {

    @NotBlank(message = "Name cannot be blank")
    public String name;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    public int age;

    @Email(message = "Invalid email format")
    public String email;

    @Size(min = 1, message = "Tags list cannot be empty")
    public List<String> tags;

    @Valid
    public Address address;

    @Valid
    public Map<String, @Min(1) Integer> scores;

    public static class Address {
        @NotNull
        public String city;

        @Pattern(regexp = "\\d{5}", message = "Zipcode must be 5 digits")
        public String zipCode;
    }
}
