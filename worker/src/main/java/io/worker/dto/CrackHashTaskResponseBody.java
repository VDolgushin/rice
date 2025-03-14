package io.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CrackHashTaskResponseBody {
    private String requestId;
    private List<String> words;
    private String taskId;
}
