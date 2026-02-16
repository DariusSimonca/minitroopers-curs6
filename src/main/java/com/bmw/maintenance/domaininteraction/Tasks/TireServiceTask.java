package com.bmw.maintenance.domaininteraction.Tasks;


import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskType;
import com.bmw.maintenance.domain.TireTask.TirePosition;
import com.bmw.maintenance.domain.TireTask.TireServiceType;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class TireServiceTask implements TaskCreator {
    @Override
    public TaskType supports() {
        return TaskType.TIRE_SERVICE;
    }

    @Override
    public MaintenanceTask create(String vin, String notes, Map<String, Object> additionalData) {
        return MaintenanceTask.createTireService(vin,notes, TireServiceType.valueOf((String) additionalData.get("tireServiceType")),TirePosition.valueOf((String) additionalData.get("tirePosition")));
    }
}
