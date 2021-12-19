package grpcutils;

/*
	Author: https://github.com/ZoneZ99
 */
public class GrpcCallerBuilder {
	private String host = "";
	private boolean isSecure = false;
	private String protoSource = "";
	private String protoFile = "";

	public GrpcCallerBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	public GrpcCallerBuilder setIsSecure(boolean isSecure) {
		this.isSecure = isSecure;
		return this;
	}

	public GrpcCallerBuilder setProtoSource(String protoSource) {
		this.protoSource = protoSource;
		return this;
	}

	public GrpcCallerBuilder setProtoFile(String protoFile) {
		this.protoFile = protoFile;
		return this;
	}

	public GrpcCaller build() {
		return new GrpcCaller(host, isSecure, protoSource, protoFile);
	}
}
