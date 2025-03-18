package io.worker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Data
@Validated
@AllArgsConstructor
public class CrackHashTaskResponseBody {

    @NotNull
    private UUID requestId;

    private List<String> words;

    @NotNull
    private String taskId;
}
