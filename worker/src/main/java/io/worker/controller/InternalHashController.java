package io.worker.controller;

import io.worker.api.InternalHashApi;
import io.worker.dto.DetailResponse;
import io.worker.dto.CrackHashTaskRequestBody;
import io.worker.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InternalHashController implements InternalHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<DetailResponse> postTask(CrackHashTaskRequestBody crackHashTaskRequestBody) {
        log.info("Post task request. Request body: {}", crackHashTaskRequestBody);
        crackHashService.crackHash(crackHashTaskRequestBody);
        return ResponseEntity.ok(new DetailResponse("Task execution started"));
    }
}
