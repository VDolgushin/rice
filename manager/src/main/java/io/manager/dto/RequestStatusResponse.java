package io.manager.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusResponse {

    private RequestStatus status;

    private List<String> data;
}
