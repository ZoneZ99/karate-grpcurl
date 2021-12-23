Feature: Test GRPC Client Stream

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

  Scenario: Test with single request
    * def requestBody = [{ "name": "Tester" }]
    * def result = client.call("com.example.grpc.GreetingService.greetingClientStream", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0].greeting == requestBody[0].name

  Scenario: Test with multiple requests
    * def requestBody = [{ "name": "Tester1" }, { "name": "Tester2" }, { "name": "Tester3" }]
    * def result = client.call("com.example.grpc.GreetingService.greetingClientStream", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0].greeting == "Tester1Tester2Tester3"

  Scenario: Test with empty name
    * def requestBody = [{ "name": "" }]
    * def result = client.call("com.example.grpc.GreetingService.greetingClientStream", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0] !contains { "name": "#string" }

  Scenario: Test with empty request body
    * def requestBody = [{}]
    * def result = client.call("com.example.grpc.GreetingService.greetingClientStream", requestBody)
    * print result
    * def responseBody = JSON.parse(result)
    * match responseBody[0] !contains { "name": "#string" }
