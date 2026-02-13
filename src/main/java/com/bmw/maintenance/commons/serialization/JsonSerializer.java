package com.bmw.maintenance.commons.serialization;

import com.bmw.maintenance.persistence.mapper.MaintenanceTaskSchemaVLatest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JsonSerializer implements VersionedSchemaSerDes<String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {
    }

    @Override
    public String serialize(VersionedSchema schema) {
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    @Override
    public VersionedSchema deserialize(String data) {
        try {
            // For simplicity, we directly deserialize to MaintenanceTaskSchemaVLatest.MaintenanceTask.
            // In a real implementation, you would inspect the JSON to determine the correct schema version and type.
            return objectMapper.readValue(data, MaintenanceTaskSchemaVLatest.MaintenanceTask.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }
}
