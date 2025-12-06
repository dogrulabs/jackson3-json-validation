package io.github.dogrulabs.jsonvalidation;

import io.github.dogrulabs.jsonvalidation.example.ComplexPojo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTest {

    private final JsonValidator validator = JsonValidators.defaultValidator();

    @Test
    void validate_shouldBeThreadSafe() throws InterruptedException, ExecutionException {
        int threads = 20;
        int iterationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        String validJson = """
                {
                  "name": "Valid",
                  "age": 20,
                  "email": "valid@test.com",
                  "tags": ["one"]
                }
                """;

        String invalidJson = """
                {
                  "name": "",
                  "age": 10
                }
                """;

        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            // Mix of valid and invalid validations
            boolean expectValid = (i % 2 == 0);
            String jsonToUse = expectValid ? validJson : invalidJson;

            tasks.add(() -> {
                for (int j = 0; j < iterationsPerThread; j++) {
                    ValidationResult result = validator.validate(jsonToUse, ComplexPojo.class);
                    if (expectValid && !result.valid())
                        return false;
                    if (!expectValid && result.valid())
                        return false;
                }
                return true;
            });
        }

        List<Future<Boolean>> results = executor.invokeAll(tasks);

        for (Future<Boolean> future : results) {
            assertTrue(future.get(), "Thread execution failed or validation result mismatch");
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }
}
