package com.malchun.approval.poc.process.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalStepProgress {
    private int stepId;
    private ApprovalStepStatus status;
    private int targetApprovals;
    private List<String> approverMails;

    public void approve(String mail) {
        if (status == ApprovalStepStatus.IN_PROGRESS) {
            this.approverMails = Stream.concat(this.approverMails.stream(), Stream.of(mail)).toList();
            if (approverMails.size() >= targetApprovals) {
                status = ApprovalStepStatus.APPROVED;
            }
        }
    }
}
