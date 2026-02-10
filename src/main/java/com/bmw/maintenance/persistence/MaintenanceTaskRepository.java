package com.bmw.maintenance.persistence;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domaininteraction.MaintenanceTasks;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class MaintenanceTaskRepository implements PanacheRepository<MaintenanceTaskEntity>, MaintenanceTasks {

    private final MaintenanceTaskMapper mapper;

    public MaintenanceTaskRepository(MaintenanceTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MaintenanceTask create(MaintenanceTask task) {
        MaintenanceTaskEntity entity = mapper.toEntity(task);
        entity.setCreatedAt(LocalDateTime.now());

        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask updateStatus(String taskId, TaskStatus newStatus) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask upsertNotes(String taskId, String notes) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        entity.setNotes(notes);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask findById(String taskId) {
        MaintenanceTaskEntity entity = findById(Long.valueOf(taskId));
        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }
        return mapper.toDomain(entity);
    }

    @Override
    public List<MaintenanceTask> findByVin(String vin) {
        List<MaintenanceTaskEntity> entities = list("vin", vin);
        return entities.stream().
                map(mapper::toDomain)
                .toList();
    }


}
