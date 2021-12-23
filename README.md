# karate-grpcurl

## About

This proof-of-concept presents a way to perform functional test on gRPC services using [karate](https://github.com/karatelabs/karate).

[grpcurl](https://github.com/fullstorydev/grpcurl) is used to call the desired gRPC service using Java code.

Request body and response are in the form of JSON array to support streaming requests and responses.
See [greetings-unary.feature](src/test/java/examples/greetings/greetings-unary.feature) for an example of how test scenario(s) should be written.

### Limitations

- Calling `grpcurl` requires having related `.proto` files for the system under test (SUT) under the `test/proto/` directory, regardless whether the SUT has enabled gRPC reflection.

## Prerequisites

- `grpcurl` is installed on machine host and can be executed, i.e. using `grpcurl <host> <method>` command.
- `Java 11` or higher.
- `Maven 3.8.3` or higher.

## Running The Example Server

```shell
mvn compile exec:java "-Dexec.mainClass=com.example.grpc.App"
```

The server will run on port 8181, i.e. `localhost:8181`.

## Running The Example Tests

```shell
mvn clean test "-Dtest=GreetingsRunner"
```
