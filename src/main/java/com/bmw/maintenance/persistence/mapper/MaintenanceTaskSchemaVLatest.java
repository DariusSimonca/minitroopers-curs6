package com.bmw.maintenance.persistence.mapper;

import com.bmw.maintenance.commons.serialization.VersionedSchema;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domain.TaskType;
import com.bmw.maintenance.domain.TireTask.TirePosition;
import com.bmw.maintenance.domain.TireTask.TireServiceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains schema definitions for maintenance task persistence.
 * <p>
 * These schemas are used as an intermediate representation between domain objects
 * and their serialized form, enabling schema versioning and migration.
 * </p>
 */

public class MaintenanceTaskSchemaVLatest {
    /**
     * Persistence schema for a maintenance task.
     * <p>
     * This schema represents version 1 of the maintenance task data structure
     * used for serialization and storage.
     * </p>
     */
    @Data
    @NoArgsConstructor
    public static class MaintenanceTask implements VersionedSchema {

        static final int SCHEMA_VERSION = 1;

        private Long taskId;
        private String vin;
        private TaskType type;
        private TaskStatus status;
        private TirePosition tirePosition;
        private TireServiceType tireServiceType;
        private String notes;

        @Override
        public int schemaVersion() {
            return SCHEMA_VERSION;
        }
    }
}
