package io.manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AddWorkerRequestBody {
    @NotNull
    private String workerURI;
}
