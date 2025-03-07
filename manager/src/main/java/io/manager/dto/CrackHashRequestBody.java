package io.manager.dto;

import lombok.Data;

@Data
public class CrackHashRequestBody {

    String hash;

    int maxLength;
}
