package com.malchun.approval.poc.process.model;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class ApprovalRequest {
    private Integer stepId;
    private String email;
}
