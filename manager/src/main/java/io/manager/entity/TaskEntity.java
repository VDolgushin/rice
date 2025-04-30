package io.manager.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    private String taskId;
    private UUID requestId;
    private String hash;
    private int maxLength;
    @With
    private int partNumber;
    private int partCount;
}
