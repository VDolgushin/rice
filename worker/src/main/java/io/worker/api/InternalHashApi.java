package io.worker.api;

import io.worker.dto.DetailResponse;
import io.worker.dto.CrackHashTaskRequestBody;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/internal/worker/")
public interface InternalHashApi {
    @PostMapping("/task")
    ResponseEntity<DetailResponse> postTask(@Valid @RequestBody CrackHashTaskRequestBody crackHashTaskRequestBody);
}
