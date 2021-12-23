Feature: Test GRPC Unary

  Background:
    * def GrpcCallerBuilder = Java.type("grpcutils.GrpcCurlBuilder");
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
    * def requestBody = [{ "name": "Tester" }]
    * def result = client.call("com.example.grpc.GreetingService.greeting", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0].greeting == requestBody[0].name

  Scenario: Test with empty name
    * def requestBody = [{ "name": "" }]
    * def result = client.call("com.example.grpc.GreetingService.greeting", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0] !contains { "name": "#string" }

  Scenario: Test with empty request body
    * def requestBody = [{}]
    * def result = client.call("com.example.grpc.GreetingService.greeting", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0] !contains { "name": "#string" }
