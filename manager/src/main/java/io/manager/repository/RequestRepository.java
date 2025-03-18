package io.manager.repository;

import io.manager.dto.RequestStatus;
import io.manager.entity.RequestEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestRepository {
    ConcurrentHashMap<UUID, RequestEntity> requestsMap = new ConcurrentHashMap<>();

    public void addRequest(UUID requestId, int completionProgress) {
        RequestEntity requestEntity = new RequestEntity(requestId, RequestStatus.IN_PROGRESS, new ArrayList<>(), completionProgress);
        requestsMap.put(requestId, requestEntity);
    }

    public RequestEntity getRequest(UUID requestId) {
        return requestsMap.get(requestId);
    }

    public void removeRequest(UUID requestId) {
        requestsMap.remove(requestId);
    }

    public void updateRequest(RequestEntity requestEntity) {
        requestsMap.put(requestEntity.getRequestId(), requestEntity);
    }
}
