package io.manager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Data
@Validated
public class CrackHashTaskRequestBody {

    @NotNull
    private String hash;

    @NotNull
    private UUID requestId;

    @NotNull
    private String taskId;

    @Min(1)
    @NotNull
    private int partNumber;

    @Min(1)
    @NotNull
    private int partCount;

    @Min(1)
    @NotNull
    private int maxLength;
}
