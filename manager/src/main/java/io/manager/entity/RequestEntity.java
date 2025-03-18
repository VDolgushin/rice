package io.manager.entity;

import io.manager.dto.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RequestEntity {
    private UUID requestId;
    private RequestStatus status;
    private List<String> data;
    private int completionProgress;
}
