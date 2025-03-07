package io.worker.controller;

import io.worker.api.InternalHashApi;
import io.worker.dto.DetailResponse;
import io.worker.dto.HashCrackTaskRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalHashController implements InternalHashApi {
    @Override
    public ResponseEntity<DetailResponse> postTask(HashCrackTaskRequestBody hashCrackTaskRequestBody) {
        return null;
    }
}
