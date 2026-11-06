package kr.fasol.obix.core.view;

import kr.fasol.obix.core.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 업무구분명 : 
 * 업무구분명 : 공통 > Jackson MessageConverter를 상속받은 클래스
 * 프로그램명 : Jackson MessageConverter를 상속받은 클래스
 * 설명 :
 *
 * 클래스 : CustomMappingJackson2HttpMessageConverter.java
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
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (log.isDebugEnabled()) {
			log.debug("### type = {}", type);
			log.debug("### object = {}", object);
		}

		if (object instanceof String) {
			String str = StringUtils.trimToEmpty((String) object);
			JsonParser jsonParser = new GsonJsonParser();
			try {
				object = jsonParser.parseMap(str);
			} catch (JsonParseException e1) {
				try {
					object = jsonParser.parseList(str);
				} catch (JsonParseException e2) {
					log.error("Object Type Converting Error. (String --> JSON Object)");
				}
			}
		}

		if (!(object instanceof Response)) {
			object = Response.ok(object);
		}

		super.writeInternal(object, type, outputMessage);
	}

}
