package io.manager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CrackHashRequestBody {

    @NotNull
    private String hash;

    @Min(1)
    @NotNull
    private int maxLength;
}
