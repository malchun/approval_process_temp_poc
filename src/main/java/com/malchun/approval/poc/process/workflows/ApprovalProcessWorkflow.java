package com.malchun.approval.poc.process.workflows;

import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessExecution;
import io.temporal.workflow.*;

@WorkflowInterface
public interface ApprovalProcessWorkflow {

    @WorkflowMethod
    void start(ApprovalProcessDefinition definition);

    @UpdateMethod
    ApprovalProcessExecution approve(String email, Integer stepId);

    @QueryMethod
    ApprovalProcessExecution getState();

    @SignalMethod
    void exit();
}
