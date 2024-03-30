package com.malchun.approval.poc.process.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class ApprovalStepDefinition {
    private Integer numberOfApprovals;
}
