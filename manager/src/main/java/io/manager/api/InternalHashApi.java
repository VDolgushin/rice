package io.manager.api;

import io.manager.dto.AddWorkerRequestBody;
import io.manager.dto.CrackHashTaskResponseBody;
import io.manager.dto.DetailResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/internal/manager/crack-hash/")
public interface InternalHashApi {
    @PostMapping("/crack-result")
    ResponseEntity<DetailResponse> postHashCrackResult(@Valid @RequestBody CrackHashTaskResponseBody crackHashTaskResponseBody);

    @PostMapping("/workers")
    ResponseEntity<DetailResponse> addWorker(@Valid @RequestBody AddWorkerRequestBody addWorkerRequestBody);

}
