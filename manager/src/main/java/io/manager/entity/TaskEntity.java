package io.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    private UUID requestId;
    private String hash;
    private int maxLength;
    @With
    private int partNumber;
    private int partCount;
    private Long createdAt;
}
