package com.malchun.approval.poc.process.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class ApprovalProcessExecution {

    LinkedHashMap<Integer, ApprovalStepProgress> stepsProgress;
    private int currentStep = 0;
    private boolean finished = false;

    public ApprovalProcessExecution(ApprovalProcessDefinition processDefinition) {
        this.stepsProgress = new LinkedHashMap<>();
        int size = processDefinition.getSteps().size();
        if (size == 0) {
            throw new IllegalArgumentException("Process definition must have at least one step");
        }
        this.stepsProgress.put(0, new ApprovalStepProgress(0, ApprovalStepStatus.IN_PROGRESS, processDefinition.getSteps().get(0).getNumberOfApprovals(), List.of()));
        for (int i = 1; i < processDefinition.getSteps().size(); i++) {
            ApprovalStepDefinition step = processDefinition.getSteps().get(i);
            this.stepsProgress.put(i, new ApprovalStepProgress(i, ApprovalStepStatus.PENDING, step.getNumberOfApprovals(), List.of()));
        }
    }

    public boolean approve(String email, Integer stepId) {
        if (finished) {
            return true;
        }
        if (stepId == this.currentStep) {
            ApprovalStepProgress stepProgress = stepsProgress.get(stepId);
            stepProgress.approve(email);
            if (stepProgress.getStatus() == ApprovalStepStatus.APPROVED) {
                if (this.currentStep == stepsProgress.size() - 1) {
                    this.finished = true;
                } else {
                    this.currentStep++;
                    stepsProgress.get(this.currentStep).setStatus(ApprovalStepStatus.IN_PROGRESS);
                }
            }
            return true;
        }
        return false;
    }


    public boolean approved() {
        return finished;
    }
}
