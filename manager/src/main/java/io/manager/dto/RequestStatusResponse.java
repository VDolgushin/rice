package io.manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class RequestStatusResponse {

    @NotNull
    private RequestStatus status;

    @NotNull
    private List<String> data;
}
