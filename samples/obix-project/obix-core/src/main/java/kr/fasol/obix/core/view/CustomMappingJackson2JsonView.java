package kr.fasol.obix.core.view;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Map;

/**
 * 업무구분명 : 
 * 업무구분명 : 공통 > Json View
 * 프로그램명 : Json View
 * 설명 :
 *
 * 클래스 : CustomMappingJackson2JsonView.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 30.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 30.         정영탁          최초생성
 */
@Slf4j
public class CustomMappingJackson2JsonView extends MappingJackson2JsonView {

	public CustomMappingJackson2JsonView() {
		super.setPrefixJson(false);                       // JSON Prefix 제거
		super.setExposePathVariables(false);              // Path Variable 노출 제거
		super.setExtractValueFromSingleKeyModel(false);   // 단일 키 모델 처리 비활성화

		ObjectMapper objectMapper = JsonMapper.builder()
				.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)  // 알파벳 순 정렬 방지
				.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false) // Map 키 정렬 방지
				.build();
		super.setObjectMapper(objectMapper);
	}

	/**
	 * JSON으로 출력할 Map 데이터 검증(Response가 아닐경우 Response로 변환하여 전송)
	 */
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 호출한 Controller.Method의 ReturnType 추출
		HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
		final Class<?> returnType;
		if (handlerMethod != null) {
			returnType = handlerMethod.getReturnType().getNestedParameterType();
		} else {
			returnType = null;
		}

		if (model != null) {
			// Return되는 ModelMap에서 Controller.Method의 ReturnType과 일치하는 데이터만 Response 되도록...
			Object modelObject;
			if (model.values().stream().anyMatch(x -> x.getClass().equals(returnType))) {
				modelObject = model.values().stream().filter(x -> x.getClass().equals(returnType)).findFirst().orElse(null);
			} else {
				modelObject = model.values().stream().findFirst().orElse(null);
			}

			// Response<?> Class Type이 아니면 Response<?> Type으로 변환하여 반환
			if (modelObject instanceof Response) {
				model = Response.convertMap((Response<?>) modelObject);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("### ReturnValue is not Response<?>.");
					log.debug("### model[{}] = {}", model.getClass().getName(), model);
					log.debug("### modelObject = {}", modelObject);
				}

				model = Response.convertMap(Response.ok(modelObject));
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("View " + formatViewName() +
					", model " + (model != null ? model : Collections.emptyMap())
					+ (getStaticAttributes().isEmpty() ? "" : ", static attributes " + getStaticAttributes()));
		}

		Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
		prepareResponse(request, response);
		renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
	}

	public void setAttributeResponse(Response<?> response) {
		Map<String, ?> model = Response.convertMap(response);
		super.setAttributesMap(model);
	}

}
