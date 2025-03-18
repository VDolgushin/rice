package io.manager.controller;

import io.manager.api.InternalHashApi;
import io.manager.dto.AddWorkerRequestBody;
import io.manager.dto.CrackHashTaskResponseBody;
import io.manager.dto.DetailResponse;
import io.manager.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class InternalHashController implements InternalHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<DetailResponse> postHashCrackResult(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        log.info("Post worker crack hash result request. Request body: {}", crackHashTaskResponseBody);
        crackHashService.completeTask(crackHashTaskResponseBody);
        return ResponseEntity.ok(new DetailResponse("Task successfully completed"));
    }

    @Override
    public ResponseEntity<DetailResponse> addWorker(AddWorkerRequestBody addWorkerRequestBody) {
        log.info("Post worker request. Request body: {}", addWorkerRequestBody);
        crackHashService.addWorker(addWorkerRequestBody);
        return ResponseEntity.ok(new DetailResponse("Worker successfully added"));
    }
}
