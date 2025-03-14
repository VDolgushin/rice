package io.worker.dto;

import lombok.Data;

@Data
public class CrackHashTaskRequestBody {
    private String hash;
    private String requestId;
    private String taskId;
    private int partNumber;
    private int partCount;
    private int maxLength;
}
