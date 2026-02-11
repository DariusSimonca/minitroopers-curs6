package com.bmw.maintenance.persistence;

import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domain.TaskType;

import java.time.LocalDateTime;

import com.bmw.maintenance.domain.TireTask.TirePosition;
import com.bmw.maintenance.domain.TireTask.TireServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence entity for maintenance tasks.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maintenance_task")
public class MaintenanceTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vin;
    private TaskType type;
    private TaskStatus status;
    private String notes;
    private TireServiceType tireServiceType;
    private TirePosition tirePosition;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
