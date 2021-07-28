
## Solution

- I've tried to implement the solution using the double loop tdd: started with an acceptance test and then move to a unit test in order to refactor the solution, nevertheless I think the tests are a but confusing.
- Although I have a mental model of what I was going to implement, I tried to let the tdd guide me
- I've started by trying to model the solution by using DDD techniques and by applying the hexagonal architecture, but at the end I'm not very happy with the solution.  I have tried to also implement the CQSR, but I think at the end the project is a bit confusing. I'm also not happy about the services distribution (application, infra and domain) and neither the entities/aggregates/value objects.
- I use RabbitMQ as the event broker to communicate decouple the responsibilities, so a possible next step would be to move the withdrawal processing to another runtime execution.

## How to run it

- I'm trying to put it inside a docker, put it's not working, it could be something on my local, because I have a Windows installation, you could try:
-- mvn clean install
-- docker-compose up -d

- Otherwise, this should work
-- Run RabbitMQ:
---    docker run --rm -it --hostname demo-tutorial-rabbit -p 15672:15672 -p 5672:5672 --net mynet rabbitmq:3-management
-- Run the application by executing its jar

## Next steps

- To actually implement CQSR
- To actually discuss about the business in order to come up with a new model
- To create a docker compose to glue everything together
-- I'm still configurung maven plugin and the docker-compose