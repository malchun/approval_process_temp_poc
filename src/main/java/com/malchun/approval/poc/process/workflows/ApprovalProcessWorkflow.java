package com.malchun.approval.poc.process.workflows;

import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessProgress;
import io.temporal.workflow.*;

@WorkflowInterface
public interface ApprovalProcessWorkflow {

    @WorkflowMethod
    void start(ApprovalProcessDefinition definition);

    @UpdateMethod
    ApprovalProcessProgress approve(String email, Integer stepId);

    @QueryMethod
    ApprovalProcessProgress getState();

    @SignalMethod
    void exit();
}
