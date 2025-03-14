package io.manager.service;

import io.manager.dto.*;
import io.manager.entity.RequestEntity;
import io.manager.entity.TaskEntity;
import io.manager.repository.RequestRepository;
import io.manager.service.mapper.RequestMapper;
import io.manager.service.mapper.TaskMapper;
import io.manager.service.workerspool.WorkersPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrackHashService {
    private final RequestRepository requestRepository;
    private final TaskMapper taskMapper;
    private final RequestMapper requestMapper;
    private final WorkersPool workersPool;


    public CrackHashResponse addRequest(CrackHashRequestBody crackHashRequestBody) {
        TaskEntity taskEntity = new TaskEntity();
        taskMapper.toModel(crackHashRequestBody, taskEntity);
        taskEntity.setRequestId(UUID.randomUUID());
        taskEntity.setCreatedAt(new Date().getTime());
        taskEntity.setPartCount(workersPool.getWorkersCount());

        requestRepository.addRequest(taskEntity.getRequestId());
        addWorkersTasks(taskEntity);

        return new CrackHashResponse(taskEntity.getRequestId().toString());
    }

    public RequestStatusResponse getRequest(String requestId) {
        RequestEntity requestEntity = requestRepository.getRequest(UUID.fromString(requestId));
        RequestStatusResponse requestStatusResponse = new RequestStatusResponse();
        requestMapper.toModel(requestEntity, requestStatusResponse);
        return requestStatusResponse;
    }

    public void completeTask(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        workersPool.completeTask(crackHashTaskResponseBody.getTaskId());
        updateRequest(crackHashTaskResponseBody);
    }

    private synchronized void updateRequest(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        var requestEntity = requestRepository.getRequest(UUID.fromString(crackHashTaskResponseBody.getRequestId()));
        if (requestEntity.getStatus().equals(RequestStatus.ERROR)) {
            return;
        }
        requestEntity.getData().addAll(crackHashTaskResponseBody.getWords());
        requestEntity.setCompletionProgress(requestEntity.getCompletionProgress() + 1);
        if (requestEntity.getCompletionProgress() == workersPool.getWorkersCount()) {
            requestEntity.setStatus(RequestStatus.READY);
        }
        requestRepository.updateRequest(requestEntity);
    }

    private void addWorkersTasks(TaskEntity taskEntity) {
        for (int i = 1; i <= workersPool.getWorkersCount(); i++) {
            workersPool.addRequest(taskEntity.withPartNumber(i));
        }
    }
}
