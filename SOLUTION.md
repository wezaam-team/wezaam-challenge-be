# Getting Started

To implemente this challenge, I have decided to create the project from scratch. To do so, I have defined the following multi-modules project based on microservice architecture:
- weezam-registry: it is based on Spring Cloud Netflix Eureka server in order to implement the discovery pattern.
- weezam-gateway: it is based on Spring Cloud Gateway. This module will be responsible for expose the API.
- weezam-user: Microservice responsible for implementing endpoints which allow us to operate with Users & PaymentMethods.
- weezam-withdrawal: Microservice responsible for implementing the logic in order to support the withdrawal processing.
- weezam-notifications: Microservice responsible for processing the notifications from withdrawal processing.

Each of them have been implemented using a [Hexagonal architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) with the following package structure:
- domain:
  - model: It contains Model classes
  - aggregate: It contains the business service classes
  - repository: It contains Repository interfaces
- adapter:
  - in
    - amqp: It contains Message Broker Listener classes
    - rest: It contains Rest API classes
  - out
    - amqp: It contains Message broker publisher classes
    - persistence: It contains Repository implementation classes
    - client: It contains Rest clients classes
- Config: It contains spring config classes.

Also, I have prepared a docker-compose file in order to run all the modules together.

# Message Broker

I am using RabbitMQ as JMS provider. Also, I am using Rabbitmq_delayed_message_exchange as plugin in order to scheduler withdrawals.
* [Scheduling messages with rabbitmq](https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq)

Apart from this, I have implemented the `dead-letter queue + parking-lot queue` approach in order to avoid losing some outgoing events about withdrawals

![img.png](docs/dl_plus_plot.png)

# Build

```
mvn clean package
```

# Run

```
docker-compose up --build
```

## Links project
- [Weezam Registry DASHBOAR](http://localhost:8761)
- [RabbitMQ](http://localhost:15672)
  - User: `guest`
  - Password: `guest`

## Postman
[Weezam Postman Collection](docs/weezam.postman_collection.json)

## Testing

I have only included some test cases in the weezam-user module. I apologise for that.