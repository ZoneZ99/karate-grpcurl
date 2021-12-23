package grpcutils;

/*
	Author: https://github.com/ZoneZ99
 */
public class GrpcCurlBuilder {
	private String host = "";
	private boolean isSecure = false;
	private String protoSource = "";
	private String protoFile = "";

	public GrpcCurlBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	public GrpcCurlBuilder setIsSecure(boolean isSecure) {
		this.isSecure = isSecure;
		return this;
	}

	public GrpcCurlBuilder setProtoSource(String protoSource) {
		this.protoSource = protoSource;
		return this;
	}

	public GrpcCurlBuilder setProtoFile(String protoFile) {
		this.protoFile = protoFile;
		return this;
	}

	public GrpcCurl build() {
		return new GrpcCurl(host, isSecure, protoSource, protoFile);
	}
}
