package io.manager.controller;

import io.manager.api.PublicHashApi;
import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashResponse;
import io.manager.dto.RequestStatusResponse;
import io.manager.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicHashController implements PublicHashApi {
    private final RequestService requestService;

    @Override
    public ResponseEntity<CrackHashResponse> crackHash(CrackHashRequestBody crackHashRequestBody) {
        requestService.addRequest();
    }

    @Override
    public ResponseEntity<RequestStatusResponse> getRequestStatus(String requestId) {
        return null;
    }
}
