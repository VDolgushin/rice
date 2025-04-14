package io.manager.entity;

import io.manager.dto.RequestStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity {
    @Id
    private String requestId;

    private RequestStatus status;

    private Set<String> data;

    private boolean [] completionProgress;
}
