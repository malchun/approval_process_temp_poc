package com.malchun.approval.poc.process.model;

import com.malchun.approval.poc.process.model.ApprovalStepDefinition;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
public class ApprovalProcessDefinition {
    private List<ApprovalStepDefinition> steps;
}
