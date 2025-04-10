package io.manager.entity;

import io.manager.dto.RequestStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity {
    @Id
    private String requestId;

    private RequestStatus status;

    private List<String> data;

    private int completionProgress;
}
