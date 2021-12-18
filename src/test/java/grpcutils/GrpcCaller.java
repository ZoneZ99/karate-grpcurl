package grpcutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GrpcCaller {

	private String host;
	private boolean isSecure;
	private String protoSource;
	private String protoFile;

	private static final int OK_CODE = 0;

	public GrpcCaller(
		String host, boolean isSecure,
		String protoSource, String protoFile
	) {
		this.host = host;
		this.isSecure = isSecure;
		this.protoSource = protoSource;
		this.protoFile = protoFile;
	}

	public String call(String method, String requestBody) {
		try {
			String[] command = this.buildCommand(method, requestBody);
			return this.executeCommand(command);
		} catch (IOException | InterruptedException e) {
			return e.getMessage();
		}
	}

	private String[] buildCommand(String method, String requestBody) {
		ArrayList<String> commandParts = new ArrayList<>(Arrays.asList(
			"grpcurl",
			"-d",
			requestBody,
			"-import-path",
			this.protoSource,
			"-proto",
			this.protoFile
		));
		if (!this.isSecure) {
			commandParts.add("-plaintext");
		}
		commandParts.add(this.host);
		commandParts.add(method);
		return commandParts.toArray(new String[0]);
	}

	private String executeCommand(String[] command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		processBuilder.directory(new File(System.getProperty("user.dir")));
		Process process = processBuilder.start();

		BufferedReader bufferedReader;
		int exitCode = process.waitFor();
		if (exitCode == OK_CODE) {
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		} else {
			bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		}

		StringBuilder output = new StringBuilder();
		bufferedReader.lines().forEach(line -> output.append(line.trim()));
		return output.toString();
	}
}
