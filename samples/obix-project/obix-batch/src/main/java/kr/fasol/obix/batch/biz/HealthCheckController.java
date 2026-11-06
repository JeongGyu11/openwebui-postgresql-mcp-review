package kr.fasol.obix.batch.biz;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.fasol.obix.core.base.Response;

@RestController
@RequestMapping("/_hcheck.json")
public class HealthCheckController {

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
	public Response<?> healthCheck() {
		Map<String, Object> healthCheckMap = new LinkedHashMap<>();

		return Response.ok(healthCheckMap);
	}
}
