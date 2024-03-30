# Approval Engine temporal PoC 
PoC for approval engine based on [temporal](https://temporal.io/)

## How to run
1. Run docker-compose
```shell
cd docker
docker-compose up
```
2. Run application
```shell
mvn spring-boot:run
```
Or use configuration in your IDE to run `ApprovalEngineApplication` class

## Description
Application implements simplified approval engine. It supports multiple stages of approval and multiple aprovers per stage.
To simulate non-deterministic behavior, there is a simulated check of aprover in external system.
It receives definition of the approval process (steps and amount of necessary approvals) and then starts the process.


## Endpoints
- POST /approval-process
    - Creates and starts new approval process from the definition in request body
    - Request body:
        ```json
        {
          "steps": [
            {
              "numberOfApprovals" : 2
            },
            {
              "numberOfApprovals" : 1
            }
          ]
        }
        ```
    - Response body:
         ```json
         {
             "id": "1"
         }
         ```
    
- GET /approval-process/{id}
    - Gets approval process by id
    - Response body:
        ```json
        {
          "stepsProgress": 
          {
            "0": 
            {
              "stepId": 0,
              "status": "APPROVED",
              "targetApprovals": 2,
              "approverMails": [
                "aaa@bbb.ccc",
                "aaa@bbb.ccc"
              ]
            },
            "1": {
              "stepId": 1,
              "status": "APPROVED",
              "targetApprovals": 1,
              "approverMails": [
                "aaa@bbb.ccc"
              ]
            }
          },
          "currentStep": 1,
          "finished": true
        }
        ```
      
- PUT /approval-process/{id}
    - Provides one approve for approval process
    - Request body:
        ```json
        {
            "stepId": 0,
            "email": "aaa@bbb.ccc"
        }
        ```    
    - Response body:
        ```json
        {
          "stepsProgress": 
          {
            "0": 
            {
              "stepId": 0,
              "status": "IN_PROGRESS",
              "targetApprovals": 2,
              "approverMails": [
                "aaa@bbb.ccc"
              ]
            },
            "1": {
              "stepId": 1,
              "status": "PENDING",
              "targetApprovals": 1,
              "approverMails": []
            }
          },
          "currentStep": 0,
          "finished": false
        }
        ```
    
- POST /approval-process/{id}/exit
    - Cancels approval process
    - Empty request body
    - Response body:
        ```json
        {
          "stepsProgress": 
          {
            "0": 
            {
              "stepId": 0,
              "status": "IN_PROGRESS",
              "targetApprovals": 2,
              "approverMails": [
                "aaa@bbb.ccc"
              ]
            },
            "1": {
              "stepId": 1,
              "status": "PENDING",
              "targetApprovals": 1,
              "approverMails": []
            }
          },
          "currentStep": 0,
          "finished": false
        }
        ```


