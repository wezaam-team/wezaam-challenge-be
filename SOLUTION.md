


Testing
Not aiming for full coverage of test cases within one test class but more for a full coverage of all classes.

1. JUnit testing to cover simple success scenarios. 
2. Controller testing - went for the restTemplate approach (expensive, full application server starts up) in the UserController class. 
Used Spring’s MockMvc to save on resources (time) for testing of the WithdrawalController. Two different methods
are used just to showcase different approaches - otherwise I would have been consistent, going with one approach for both services.
These tests could have been narrowed only to the web layer by using @WebMvcTest.