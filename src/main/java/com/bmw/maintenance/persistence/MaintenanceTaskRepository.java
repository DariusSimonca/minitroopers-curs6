package com.bmw.maintenance.persistence;

import com.bmw.maintenance.commons.serialization.VersionedSchemaSerDes;
import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domaininteraction.MaintenanceTasks;
import com.bmw.maintenance.persistence.mapper.MaintenanceTaskMapper;
import com.bmw.maintenance.persistence.mapper.MaintenanceTaskSchemaVLatest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class MaintenanceTaskRepository implements PanacheRepository<MaintenanceTaskEntity>, MaintenanceTasks {

    private final MaintenanceTaskMapper mapper;
    private final VersionedSchemaSerDes<String> serializer;

    public MaintenanceTaskRepository(MaintenanceTaskMapper mapper, VersionedSchemaSerDes<String> serializer) {
        this.mapper = mapper;
        this.serializer = serializer;
    }

    @Override
    public MaintenanceTask create(MaintenanceTask task) {
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema = mapper.toSchema(task);
        String serializedAggregate = serializer.serialize(schema);

        // Create entity with serialized aggregate
        MaintenanceTaskEntity entity = new MaintenanceTaskEntity();
        entity.setAggregate(serializedAggregate);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        persist(entity);
        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask updateStatus(String taskId, TaskStatus newStatus) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        // Deserialize aggregate to schema
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        // Update schema
        schema.setStatus(newStatus);

        // Serialize updated schema
        String serializedAggregate = serializer.serialize(schema);
        entity.setAggregate(serializedAggregate);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask upsertNotes(String taskId, String notes) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        // Update schema
        schema.setNotes(notes);

        // Serialize updated schema
        String serializedAggregate = serializer.serialize(schema);
        entity.setAggregate(serializedAggregate);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask findById(String taskId) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));
        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        return mapper.toDomain(schema);
    }

    @Override
    public List<MaintenanceTask> findAllTasks() {
        List<MaintenanceTaskEntity> entities = listAll();

        return entities.stream()
                .map(entity -> {
                    MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                            (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());
                    return mapper.toDomain(schema);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceTask> findByVin(String vin) {
        List<MaintenanceTaskEntity> entities = list("vin", vin);
        return entities.stream()
                .map(entity -> {
                    MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                            (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());
                    return mapper.toDomain(schema);
                })
                .filter(task -> vin.equals(task.getVin()))
                .collect(Collectors.toList());
    }


}
