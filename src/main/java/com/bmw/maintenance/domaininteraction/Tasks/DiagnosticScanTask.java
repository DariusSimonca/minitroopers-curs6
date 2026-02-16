package com.bmw.maintenance.domaininteraction.Tasks;

import com.bmw.maintenance.domain.DiagnosticScan.ErrorCodes;
import com.bmw.maintenance.domain.DiagnosticScan.ScannerType;
import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskType;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DiagnosticScanTask implements TaskCreator {
    @Override
    public TaskType supports() {
        return TaskType.DIAGNOSTIC_SCAN;
    }

    @Override
    public MaintenanceTask create(String vin, String notes, Map<String, Object> additionalData) {

        List<String> codes = (List<String>) additionalData.get("errorCodes");
        Set<ErrorCodes> errorCodes = codes.stream()
                .map(ErrorCodes::valueOf)
                .collect(Collectors.toSet());

        return MaintenanceTask.createDiagnosticScanService(vin,notes, ScannerType.valueOf((String) additionalData.get("scannerType")), errorCodes);
    }
}
