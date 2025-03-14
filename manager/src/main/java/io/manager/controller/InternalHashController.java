package io.manager.controller;

import io.manager.api.InternalHashApi;
import io.manager.dto.CrackHashTaskResponseBody;
import io.manager.dto.DetailResponse;
import io.manager.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InternalHashController implements InternalHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<DetailResponse> postHashCrackResult(CrackHashTaskResponseBody crackHashTaskResponseBody) {
        crackHashService.completeTask(crackHashTaskResponseBody);
        return ResponseEntity.ok(new DetailResponse("Task completed"));
    }
}
