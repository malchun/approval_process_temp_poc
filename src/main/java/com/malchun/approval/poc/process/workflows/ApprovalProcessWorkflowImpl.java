package com.malchun.approval.poc.process.workflows;

import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessExecution;
import com.malchun.approval.poc.process.activities.ApproveValidatorActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@WorkflowImpl(taskQueues="ApprovalProcessQueue")
@Slf4j
public class ApprovalProcessWorkflowImpl implements ApprovalProcessWorkflow {

    private ApprovalProcessExecution approvalProcessExecution;
    private boolean exit = false;
    private ApproveValidatorActivity approveValidatorActivity =
            Workflow.newActivityStub(
                    ApproveValidatorActivity.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

    @Override
    public void start(ApprovalProcessDefinition definition) {
        log.debug("Starting approval process");
        this.approvalProcessExecution = new ApprovalProcessExecution(definition);
        log.debug("Process starting");
        Workflow.await(() -> exit);
    }

    @Override
    public ApprovalProcessExecution approve(String email, Integer stepId) {
        log.debug("Approving request: {}", email);
        if (approvalProcessExecution.approved()) {
            this.exit = true;
            return approvalProcessExecution;
        }
        log.debug("Not approved yet");
        if (approveValidatorActivity.validateApprove(email)) {
            approvalProcessExecution.approve(email, stepId);
        }
        log.debug("Process state: {}", approvalProcessExecution);
        return approvalProcessExecution;
    }

    @Override
    public ApprovalProcessExecution getState() {
        log.debug("Returning process state: {}", approvalProcessExecution);
        return approvalProcessExecution;
    }

    @Override
    public void exit() {
        this.exit = true;
    }
}
