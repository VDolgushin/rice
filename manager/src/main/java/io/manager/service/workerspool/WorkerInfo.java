package io.manager.service.workerspool;

import io.manager.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerInfo {
    WorkerStatus workerStatus;
    TaskEntity currentTask;
}
