package io.manager.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class HealthResponse {
    private String status;
}
