package io.manager.api;

import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashResponse;
import io.manager.dto.RequestStatusResponse;
import io.manager.exception.NoWorkersAvailableException;
import io.manager.exception.RequestNotFoundException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = "/crack-hash/")
public interface PublicHashApi {

    @PostMapping("/request")
    ResponseEntity<CrackHashResponse> crackHash(@Valid @RequestBody CrackHashRequestBody crackHashRequestBody) throws NoWorkersAvailableException;

    @GetMapping("/request/{request_id}")
    ResponseEntity<RequestStatusResponse> getRequestStatus(@Valid @PathVariable(name = "request_id") UUID requestId) throws RequestNotFoundException;

}
