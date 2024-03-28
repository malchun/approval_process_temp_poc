package com.malchun.approval.poc.process.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ApproveValidatorActivity {
    Boolean validateApprove(String approverMail);
}

