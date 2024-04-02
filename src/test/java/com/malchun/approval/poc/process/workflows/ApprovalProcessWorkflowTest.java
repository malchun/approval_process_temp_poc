package com.malchun.approval.poc.process.workflows;

import com.malchun.approval.poc.process.activities.ApproveValidatorActivityImpl;
import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessProgress;
import com.malchun.approval.poc.process.model.ApprovalStepDefinition;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

@Slf4j
@Disabled
public class ApprovalProcessWorkflowTest {


    @RegisterExtension
    public TestWorkflowExtension testWorkflowExtension = TestWorkflowExtension.newBuilder()
//            .setActivityImplementations(new ApproveValidatorActivityImpl())
            .setWorkflowTypes(ApprovalProcessWorkflowImpl.class)
            .setDoNotStart(true)
            .build();

    @Test
    public void testStart(TestWorkflowEnvironment testEnv, Worker worker, ApprovalProcessWorkflow approvalProcessWorkflowImpl) {
        // Given
        var approvalDefinition = ApprovalProcessDefinition.builder()
                                       .steps(List.of(ApprovalStepDefinition.builder().numberOfApprovals(1).build())).build();
        var approvalProgress = new ApprovalProcessProgress(approvalDefinition);
        testEnv.start();


        // When
        approvalProcessWorkflowImpl.start(approvalDefinition);

        // Then
        assert(approvalProcessWorkflowImpl.getState()).equals(approvalProgress);
    }
}
