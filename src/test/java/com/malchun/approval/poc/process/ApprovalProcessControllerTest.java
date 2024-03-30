package com.malchun.approval.poc.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malchun.approval.poc.process.model.ApprovalProcessDefinition;
import com.malchun.approval.poc.process.model.ApprovalProcessExecution;
import com.malchun.approval.poc.process.model.ApprovalRequest;
import com.malchun.approval.poc.process.model.ApprovalStepDefinition;
import com.malchun.approval.poc.process.workflows.ApprovalProcessWorkflow;
import com.malchun.approval.poc.process.workflows.ApprovalProcessWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ApprovalProcessController.class)
public class ApprovalProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkflowClient workflowClient;

    private ApprovalProcessController approvalProcessController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void startApprovalProcessTest() throws Exception {
        // Given
        var approvalProcessDefinition = ApprovalProcessDefinition.builder()
                .steps(List.of(
                        ApprovalStepDefinition.builder().numberOfApprovals(1).build(),
                        ApprovalStepDefinition.builder().numberOfApprovals(2).build()
                ))
                .build();
        var workflow = mock(ApprovalProcessWorkflow.class);
        when(workflowClient.newWorkflowStub(eq(ApprovalProcessWorkflow.class), any(WorkflowOptions.class)))
                .thenReturn(workflow);

        // When
        var result = mockMvc.perform(post("/approval-process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvalProcessDefinition)));

        // Then
        result.andExpect(status().isOk());
        verify(workflow).start(approvalProcessDefinition);
    }

    @Test
    public void getProcessStatusTest() throws Exception {
        // Given
        var workflow = mock(ApprovalProcessWorkflow.class);
        var approvalProcessExecution = new ApprovalProcessExecution(ApprovalProcessDefinition.builder()
                .steps(List.of(ApprovalStepDefinition.builder().numberOfApprovals(1).build())).build());
        when(workflowClient.newWorkflowStub(eq(ApprovalProcessWorkflow.class), anyString()))
                .thenReturn(workflow);
        when(workflow.getState()).thenReturn(approvalProcessExecution);

        // When
        var result = mockMvc.perform(get("/approval-process/123"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(approvalProcessExecution)));
    }

    @Test
    public void approveTest() throws Exception {
        // Given
        var approvalRequest = ApprovalRequest.builder()
                .email("aaa@bbb.ccc").stepId(0).build();
        var workflow = mock(ApprovalProcessWorkflow.class);
        when(workflowClient.newWorkflowStub(eq(ApprovalProcessWorkflow.class), anyString()))
                .thenReturn(workflow);

        // When

        var result = mockMvc.perform(put("/approval-process/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvalRequest)));

        // Then
        result.andExpect(status().isOk());
        verify(workflow).approve(approvalRequest.getEmail(), approvalRequest.getStepId());
    }

    @Test
    public void exitTest() throws Exception {
        // Given
        var workflow = mock(ApprovalProcessWorkflow.class);
        when(workflowClient.newWorkflowStub(eq(ApprovalProcessWorkflow.class), anyString()))
                .thenReturn(workflow);

        // When
        var result = mockMvc.perform(post("/approval-process/123/exit"));

        // Then
        result.andExpect(status().isOk());
        verify(workflow).exit();
    }

}
