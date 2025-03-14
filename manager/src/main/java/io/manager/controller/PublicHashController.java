package io.manager.controller;

import io.manager.api.PublicHashApi;
import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashResponse;
import io.manager.dto.RequestStatusResponse;
import io.manager.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicHashController implements PublicHashApi {
    private final CrackHashService crackHashService;

    @Override
    public ResponseEntity<CrackHashResponse> crackHash(CrackHashRequestBody crackHashRequestBody) {
        CrackHashResponse crackHashResponse = crackHashService.addRequest(crackHashRequestBody);
        return ResponseEntity.ok(crackHashResponse);
    }

    @Override
    public ResponseEntity<RequestStatusResponse> getRequestStatus(String requestId) {
        RequestStatusResponse requestStatusResponse = crackHashService.getRequest(requestId);
        return ResponseEntity.ok(requestStatusResponse);
    }
}
