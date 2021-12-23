package grpcutils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
	Author: https://github.com/ZoneZ99
 */
public class GrpcCurl implements GrpcCaller {

	private String host;
	private boolean isSecure;
	private String protoSource;
	private String protoFile;

	private static final int OK_CODE = 0;

	public GrpcCurl(
		String host, boolean isSecure,
		String protoSource, String protoFile
	) {
		this.host = host;
		this.isSecure = isSecure;
		this.protoSource = protoSource;
		this.protoFile = protoFile;
	}

	public String call(String method, List<Map<String, Object>> requestBody) {
		try {
			String[] command = this.buildCommand(method);
			return this.executeCommand(command, requestBody);
		} catch (IOException | InterruptedException e) {
			return e.getMessage();
		}
	}

	private String[] buildCommand(String method) {
		ArrayList<String> commandParts = new ArrayList<>(Arrays.asList(
			"grpcurl",
			"-d",
			"@",
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

	private String executeCommand(String[] command, List<Map<String, Object>> input) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		processBuilder.directory(new File(System.getProperty("user.dir")));
		Process process = processBuilder.start();
		OutputStream stdin = process.getOutputStream();

		try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(stdin))) {
			ObjectMapper objectMapper = new ObjectMapper();
			for (Map<String, Object> messageMap : input) {
				String message = objectMapper.writeValueAsString(messageMap);
				inputWriter.write(message);
				inputWriter.flush();
			}
		}
		int exitCode = process.waitFor();

		BufferedReader bufferedReader;
		if (exitCode == OK_CODE) {
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		} else {
			bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		}

		StringBuilder outputBuilder = new StringBuilder();
		bufferedReader.lines().forEach(l -> outputBuilder.append(l.trim()));

		return this.processCallResult(outputBuilder.toString());
	}

	private String processCallResult(String rawResult) throws IOException {
		if (!rawResult.startsWith("{") || !rawResult.endsWith("}")) {
			return rawResult;
		}
		return new JsonMapper().readerFor(JsonNode.class).readValues(rawResult).readAll().toString();
	}
}
