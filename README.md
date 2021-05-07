### Technical challenge (for developers)

Assume we have a big legacy system and one of the parts is withdrawal processing (the process that allows to transfer money from company to employee accounts). Now we have chance to completely rewrite the system, including API change (endpoints, DTOs etc). As a techical challenge we suggest you to take it. You can do whathever you want following the acceptance criteria:

- Use any architecture you are comfortable with 
- Use modern Java or Kotlin
- Use Spring boot
- Use any database SQL/NoSQL (please use embedded)
- The code must be tested
- The service should be easy to run (e.q. docker-compose)

#### Here are some business rules of the processing:

- We have a list of users (`/v1/users` endpoint)
- A user has several payment methods
- A user can execute a withdrawal request using one of a payment methods
- A withdrawal can be executed as soon as possible or be scheduled to execute later
- After the service receives a request it stores a withdrawal object in our DB and sends a request to a payment provider async. Note: for this task we don't care about a transaction completion  
- We MUST 100% send notifications regarding any withdrawal status (event, email etc)

#### Steps to proceed:

- Fork the repository (please use private repository)
- Implement your nice solution
- Once complete invite `makcon` and `pbravowezaam`
