package io.worker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@AllArgsConstructor
public class AddWorkerRequestBody {
    @NotNull
    private String workerURI;
}
