package io.manager.controller;

import io.manager.api.InternalHashApi;
import io.manager.dto.DetailResponse;
import io.manager.dto.HashCrackResultRequestBody;
import org.springframework.http.ResponseEntity;

public class InternalHashController implements InternalHashApi {
    @Override
    public ResponseEntity<DetailResponse> postHashCrackResult(HashCrackResultRequestBody hashCrackResultRequestBody) {
        return null;
    }
}
