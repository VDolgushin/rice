package io.worker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class DetailResponse {
    @NotNull
    @Schema(example = "detailed explanation of result")
    private final String detail;
}
