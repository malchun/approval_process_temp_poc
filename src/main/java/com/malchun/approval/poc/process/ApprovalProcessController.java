package com.malchun.approval.poc.process;

import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessExecution;
import com.malchun.approval.poc.process.model.ApprovalRequest;
import com.malchun.approval.poc.process.workflows.ApprovalProcessWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/approval-process")
@AllArgsConstructor
@Slf4j
public class ApprovalProcessController {

    private WorkflowClient workflowClient;

    @PostMapping
    @ResponseBody
    public String start(@RequestBody ApprovalProcessDefinition definition) {
        String id = UUID.randomUUID().toString();
        ApprovalProcessWorkflow workflow = workflowClient.newWorkflowStub(ApprovalProcessWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue("ApprovalProcessQueue")
                        .setWorkflowExecutionTimeout(Duration.ofSeconds(1000))
                        .build());
        CompletableFuture.runAsync(() -> workflow.start(definition));
        return id;
    }


    @PostMapping("/{id}/exit")
    public void exit(@PathVariable("id") String id) {
        workflowClient.newWorkflowStub(ApprovalProcessWorkflow.class, id).exit();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ApprovalProcessExecution getProcessStatus(@PathVariable("id") String id) {
        log.debug("Getting process status for id: {}", id);
        ApprovalProcessExecution state = workflowClient.newWorkflowStub(ApprovalProcessWorkflow.class, id).getState();
        return state;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ApprovalProcessExecution approve(@PathVariable("id") String id, @RequestBody ApprovalRequest request) {
        log.debug("Approving request: {}", request);
        ApprovalProcessExecution state = workflowClient.newWorkflowStub(ApprovalProcessWorkflow.class, id).approve(request.getEmail(), request.getStepId());
        return state;
    }

}
