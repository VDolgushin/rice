package io.manager.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrackHashTaskResponseBody {
    private String requestId;
    private List<String> words;
    private String taskId;
}
