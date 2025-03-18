package io.manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Data
@Validated
public class CrackHashTaskResponseBody {

    @NotNull
    private UUID requestId;

    private List<String> words;

    @NotNull
    private String taskId;
}
