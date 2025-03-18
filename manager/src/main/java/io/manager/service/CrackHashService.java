package io.manager.service;

import io.manager.dto.*;
import io.manager.entity.RequestEntity;
import io.manager.entity.TaskEntity;
import io.manager.exception.NoWorkersAvailableException;
import io.manager.exception.RequestNotFoundException;
import io.manager.repository.RequestRepository;
import io.manager.service.mapper.RequestMapper;
import io.manager.service.mapper.TaskMapper;
import io.manager.service.workerspool.WorkersPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrackHashService {
    private final RequestRepository requestRepository;
    private final TaskMapper taskMapper;
    private final RequestMapper requestMapper;
    private final WorkersPool workersPool;


    public CrackHashResponse addRequest(CrackHashRequestBody crackHashRequestBody) throws NoWorkersAvailableException {
        TaskEntity taskEntity = new TaskEntity();
        taskMapper.toModel(crackHashRequestBody, taskEntity);
        taskEntity.setRequestId(UUID.randomUUID());

        int workersCount = workersPool.getWorkersCount();

        if(workersCount == 0){
            throw new NoWorkersAvailableException("No available workers at the moment, please try again later");
        }

        taskEntity.setPartCount(workersCount);
        requestRepository.addRequest(taskEntity.getRequestId(), workersCount);
        addWorkersTasks(taskEntity);

        log.info("Crack hash request: {} successfully added", taskEntity);
        return new CrackHashResponse(taskEntity.getRequestId());
    }

    public RequestStatusResponse getRequest(UUID requestId) throws RequestNotFoundException {
        RequestEntity requestEntity = requestRepository.getRequest(requestId);
        if(requestEntity == null){
            throw new RequestNotFoundException("Request with id: " + requestId + " not found");
        }
        RequestStatusResponse requestStatusResponse = new RequestStatusResponse();
        requestMapper.toModel(requestEntity, requestStatusResponse);
        return requestStatusResponse;
    }

    public void completeTask(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        workersPool.completeTask(crackHashTaskResponseBody.getTaskId());
        updateRequest(crackHashTaskResponseBody);
        log.info("Task with id: {} successfully completed", crackHashTaskResponseBody.getTaskId());
    }

    public void addWorker(AddWorkerRequestBody addWorkerRequestBody){
        workersPool.addWorker(addWorkerRequestBody.getWorkerURI());
        log.info("Worker: {} successfully added", addWorkerRequestBody);
    }

    private synchronized void updateRequest(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        var requestEntity = requestRepository.getRequest(crackHashTaskResponseBody.getRequestId());
        if (requestEntity.getStatus().equals(RequestStatus.ERROR)) {
            return;
        }
        log.info("Updating request: {}", requestEntity);
        requestEntity.getData().addAll(crackHashTaskResponseBody.getWords());
        requestEntity.setCompletionProgress(requestEntity.getCompletionProgress() - 1);
        if (requestEntity.getCompletionProgress() == 0) {
            requestEntity.setStatus(RequestStatus.READY);
        }
        requestRepository.updateRequest(requestEntity);
        log.info("Request successfully updated: {}", requestEntity);
    }

    private void addWorkersTasks(TaskEntity taskEntity) {
        for (int i = 1; i <= taskEntity.getPartCount(); i++) {
            workersPool.addRequest(taskEntity.withPartNumber(i));
        }
    }
}
