package io.github.dogrulabs.jsonvalidation.example;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Example DTO for documentation and testing usage.
 */
public class MappingDocument {

    private Map<String, MappedAttribute> mappedAttributes;

    private Map<String, String> sources;

    @NotNull
    @Valid
    public Map<String, MappedAttribute> getMappedAttributes() {
        return mappedAttributes;
    }

    public void setMappedAttributes(Map<String, MappedAttribute> mappedAttributes) {
        this.mappedAttributes = mappedAttributes;
    }

    public Map<String, String> getSources() {
        return sources;
    }

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }

    public static class MappedAttribute {
        private String value;
        private String label;

        private Double confidence;

        private List<Conflict> conflicts;

        // Getters and Setters
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @DecimalMin("0.0")
        @DecimalMax("1.0")
        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }

        @Valid
        public List<Conflict> getConflicts() {
            return conflicts;
        }

        public void setConflicts(List<Conflict> conflicts) {
            this.conflicts = conflicts;
        }
    }

    public static class Conflict {
        private String sourceId;

        @NotNull
        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }
    }
}
