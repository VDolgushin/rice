package io.worker.controller;

import io.worker.api.InternalHashApi;
import io.worker.dto.DetailResponse;
import io.worker.dto.CrackHashTaskRequestBody;
import io.worker.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalHashController implements InternalHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<DetailResponse> postTask(CrackHashTaskRequestBody crackHashTaskRequestBody) {
        crackHashService.crackHash(crackHashTaskRequestBody);
        return ResponseEntity.ok(new DetailResponse("Task execution started"));
    }
}
