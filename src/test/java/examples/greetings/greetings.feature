Feature: Test GRPC

  Background:
    * def GrpcCallerBuilder = Java.type("grpcutils.GrpcCallerBuilder");
    * def client =
      """
      new GrpcCallerBuilder()
        .setProtoSource("src/test/proto")
        .setProtoFile("GreetingService.proto")
        .setHost("localhost:8181")
        .setIsSecure(false)
        .build();
      """

  Scenario: Test with name
    * def requestBody = { "name": "Tester" }
    * def result = client.call("com.example.grpc.GreetingService.greeting", JSON.stringify(requestBody))
    * def responseBody = JSON.parse(result)
    * print responseBody
    * match responseBody.greeting == requestBody.name

  Scenario: Test with empty name
    * def requestBody = { "name": "" }
    * def result = client.call("com.example.grpc.GreetingService.greeting", JSON.stringify(requestBody))
    * def responseBody = JSON.parse(result)
    * print responseBody
    * match responseBody == {}

  Scenario: Test with empty request body
    * def requestBody = {}
    * def result = client.call("com.example.grpc.GreetingService.greeting", JSON.stringify(requestBody))
    * def responseBody = JSON.parse(result)
    * print responseBody
    * match responseBody == {}
