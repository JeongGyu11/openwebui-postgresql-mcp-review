package kr.fasol.obix.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JSON 변환 유틸
 */
@Component
public class JsonUtil {
	private static ObjectMapper objectMapper;

	@Autowired
	public JsonUtil(ObjectMapper mapper) {
		objectMapper = mapper;
	}

	/**
	 * Object 객체를 JSON 문자열로 변환
	 */
	public static String ObjectToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * JSON 문자열을 Object 객체로 변환
	 */
	public static <T> T JsonToObject(String json, Class<T> classType) throws JsonProcessingException {
		return objectMapper.readValue(json, classType);
	}
	public static <T> T JsonToObject(String json, TypeReference<T> valueTypeRef) throws JsonProcessingException {
		return objectMapper.readValue(json, valueTypeRef);
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		return objectMapper.convertValue(fromValue, toValueType);
	}
	public static <T> T convertValue(Object fromValue, TypeReference<T> valueTypeRef) {
		return objectMapper.convertValue(fromValue, valueTypeRef);
	}

}
