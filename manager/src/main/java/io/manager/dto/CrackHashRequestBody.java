package io.manager.dto;

import lombok.Data;

@Data
public class CrackHashRequestBody {

    private String hash;

    private int maxLength;
}
