package kr.fasol.obix.front.common.thymeleaf.processor;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import kr.fasol.obix.core.util.WebUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import com.google.gson.Gson;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > Request Data 출력
 * 프로그램명 : Request Data 출력
 * 설명 : 
 * <pre>
 * <html xmlns:obix="http://obix.fasol.co.kr/thymeleaf/layout">
 *
 * <script th:inline="javascript" type="text/javascript">
 *     var requestData = [# obix:requestData=""][/];
 *     console.log(requestData);
 * </script>
 * </pre>
 *
 * 클래스 : RequestDataTagProcessor.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 4.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 4.        정영탁			  최초작성
 */
public final class RequestDataTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "requestData";
	private static final int PRECEDENCE = 10000;

    public RequestDataTagProcessor(final String dialectPrefix) {
		super(TemplateMode.JAVASCRIPT		// JAVASCRIPT 모드로 설정
				, dialectPrefix		// 접두사
				, null				// 태그명 미설정. 적용가능 태그명에 제약이 없다.
				, false				// 태그명에 접두사를 적용하지 않는다.
				, ATTR_NAME			// 적용될 속성명
				, true				// 속성명에 dialect 접두사를 사용한다.
				, PRECEDENCE		// dialect 들 내에서 적용 우선권.
				, true				// 적용한 속성명은 처리 후 삭제한다.
		);
    }

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {

        Map<String, Object> reqMap = WebUtil.parameterMap(getHttpServletRequest());

        StringBuilder result = new StringBuilder();
        result.append( new Gson().toJson(reqMap) );

		// 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
