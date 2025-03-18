package io.manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Data
@Validated
@AllArgsConstructor
public class CrackHashResponse {
    @NotNull
    private UUID requestId;
}
