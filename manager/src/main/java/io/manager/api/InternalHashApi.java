package io.manager.api;

import io.manager.dto.CrackHashTaskResponseBody;
import io.manager.dto.DetailResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/internal/api/manager/hash/")
public interface InternalHashApi {
    @PostMapping("/crack/request")
    ResponseEntity<DetailResponse> postHashCrackResult(@Valid @RequestBody CrackHashTaskResponseBody crackHashTaskResponseBody);

}
