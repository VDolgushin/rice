package io.manager.api;

import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashResponse;
import io.manager.dto.RequestStatusResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/hash/")
public interface PublicHashApi {

    @PostMapping("/crack")
    ResponseEntity<CrackHashResponse> crackHash(@Valid @RequestBody CrackHashRequestBody crackHashRequestBody);

    @GetMapping("/status")
    ResponseEntity<RequestStatusResponse> getRequestStatus(@Valid @RequestParam(name = "requestId") String requestId);

}
