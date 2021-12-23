package grpcutils;

import java.util.List;
import java.util.Map;

public interface GrpcCaller {

	String call(String method, List<Map<String, Object>> requestBody);
}
