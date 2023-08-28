##Notes on Transaction Completion
As per README: "for this task, we don't care about a transaction completion". Therefore, there
is no component to process transactions (or keep track of any account balance).

##Logging
For brevity the implemented solution does not have any logging or tracing capabilities.

##Swagger UI
Available at: http://<host>:<port>/swagger-ui/

##RabbitMQ
To run RabbitMQ in Docker use the script provided: ```./scripts/start-rabbitmq-docker.sh```
RabbitMQ management console available at: http://localhost:15672

##Running locally
For local testing use the local profile ```-Dspring.profiles.active=local```

##Testing
1. Scope limited to Unit (with Mocks) and simple integration testing.
2. Controller testing - went for the restTemplate approach (expensive, full application server starts up) in the UserController class. 
Used Spring’s MockMvc to save on resources (time) for testing of the WithdrawalController. Two different methods
are used just to showcase different approaches - otherwise I would have been consistent, going with one approach for both services.
Furthermore, these tests could have been narrowed only to the web layer by using @WebMvcTest.

Not covered: 
- comprehensive integration testing, 
- functional testing,
- performance testing.

##Bonus problem to solve
###Description of the problem
We noticed that in the current solution, we are losing some outgoing events about withdrawalInstants. We MUST 100% notify listeners 
regarding any withdrawalInstant statuses. For example: in WithdrawalService.processScheduledmethod, we updated the status to processing 
in the database (line 89), and then we sent an event (line 90). What if the event failed to send (e.q. connection issues to a messaging provider)?
###Possible solution
One solution is to have a scheduler picking up from the DB, with some delay (the delay is there so that transaction confirmation can arrive 
before re-processing the withdrawal), all the transactions with the PROCESSING status (both instant and scheduled withdrawals) to try to re-process them.
In parallel to this, we could have a service/process that listens to the ack() coming over the event-bus (AMQP topic) from the transaction processing entity. 
If we see confirmation of the transaction fully processed, then we can set the status of this particular withdrawal request in the DB to SUCCESS so that 
it is not picked up again for re-processing.