package com.example.grpc;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
	@Override
	public void greeting(
		GreetingServiceOuterClass.HelloRequest request,
		StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
	) {
		// HelloRequest has toString auto-generated.
		System.out.println(request);

		// You must use a builder to construct a new Protobuffer object
		GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
			.setGreeting(request.getName())
			.addAllHobbies(request.getHobbiesList())
			.setInner(
				GreetingServiceOuterClass.InnerStruct.newBuilder()
					.setValue("Inner")
					.addRepeated("Repeated List")
					.build()
			)
			.build();
		System.out.println("Unary response: " + response);

		// Use responseObserver to send a single response back
		responseObserver.onNext(response);

		// When you are done, you must call onCompleted.
		responseObserver.onCompleted();
	}

	@Override
	public void greetingServerStream(
		GreetingServiceOuterClass.HelloRequest request,
		StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
	) {
		System.out.println(request);
		for (int i = 0; i < 10; i++) {
			GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
				.setGreeting(request.getName())
				.addAllHobbies(request.getHobbiesList())
				.setInner(
					GreetingServiceOuterClass.InnerStruct.newBuilder()
						.setValue("Inner")
						.addRepeated("Repeated List")
						.build()
				)
				.build();
			System.out.println("Server stream response " + i + " : " + response);
			responseObserver.onNext(response);
		}
		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<GreetingServiceOuterClass.HelloRequest> greetingClientStream(
		StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
	) {
		return new StreamObserver<GreetingServiceOuterClass.HelloRequest>() {
			String name;
			List<String> hobbies = new ArrayList<>();

			@Override
			public void onNext(GreetingServiceOuterClass.HelloRequest helloRequest) {
				this.name = helloRequest.getName();
				this.hobbies.addAll(helloRequest.getHobbiesList());
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println(throwable.getMessage());
			}

			@Override
			public void onCompleted() {
				GreetingServiceOuterClass.HelloResponse response =
					GreetingServiceOuterClass.HelloResponse.newBuilder()
						.setGreeting(this.name)
						.addAllHobbies(this.hobbies)
						.setInner(
							GreetingServiceOuterClass.InnerStruct.newBuilder()
								.setValue("Inner")
								.addRepeated("Repeated List")
								.build()
						)
					.build();
				System.out.println("Client stream response: " + response);
				responseObserver.onNext(response);

				responseObserver.onCompleted();
			}
		};
	}

	@Override
	public StreamObserver<GreetingServiceOuterClass.HelloRequest> greetingBidirectionalStream(
		StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver
	) {
		return new StreamObserver<GreetingServiceOuterClass.HelloRequest>() {

			@Override
			public void onNext(GreetingServiceOuterClass.HelloRequest helloRequest) {
				for (int i = 0; i < 10; i++) {
					GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
						.setGreeting(helloRequest.getName())
						.addAllHobbies(helloRequest.getHobbiesList())
						.setInner(
							GreetingServiceOuterClass.InnerStruct.newBuilder()
								.setValue("Inner")
								.addRepeated("Repeated List")
								.build()
						)
						.build();
					System.out.println("Bidirectional stream response " + i + " : " + response);
					responseObserver.onNext(response);
				}
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println(throwable.getMessage());
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}
