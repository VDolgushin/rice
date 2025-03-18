package io.manager.controller;

import io.manager.api.PublicHashApi;
import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashResponse;
import io.manager.dto.RequestStatusResponse;
import io.manager.exception.NoWorkersAvailableException;
import io.manager.exception.RequestNotFoundException;
import io.manager.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicHashController implements PublicHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<CrackHashResponse> crackHash(CrackHashRequestBody crackHashRequestBody) throws NoWorkersAvailableException {
        log.info("Post crack hash request. Request body: {}", crackHashRequestBody);
        CrackHashResponse crackHashResponse = crackHashService.addRequest(crackHashRequestBody);
        return ResponseEntity.ok(crackHashResponse);
    }

    @Override
    public ResponseEntity<RequestStatusResponse> getRequestStatus(UUID requestId) throws RequestNotFoundException {
        log.info("Get request status request. Request id: {}", requestId);
        RequestStatusResponse requestStatusResponse = crackHashService.getRequest(requestId);
        return ResponseEntity.ok(requestStatusResponse);
    }
}
