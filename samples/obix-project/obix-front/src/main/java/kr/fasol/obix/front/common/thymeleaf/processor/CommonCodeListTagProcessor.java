package kr.fasol.obix.front.common.thymeleaf.processor;

import kr.fasol.obix.front.common.util.CommonCodeHelper;
import kr.fasol.obix.front.dto.comm.CmnCdQryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > 공통코드 목록
 * 프로그램명 : 공통코드 목록
 * 설명 : 
 * <pre>
 * <html xmlns:obix="http://obix.fasol.co.kr/thymeleaf/layout">
 *
 * <script th:inline="javascript" type="text/javascript">
 *     var commonCodeList = [# obix:commonCodeList="ADM_USER_STUS_CD"][/];
 *     console.log(commonCodeList);
 * </script>
 * </pre>
 *
 * 클래스 : CommonCodeListTagProcessor.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 4.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 4.        정영탁			  최초작성
 */
@Slf4j
public final class CommonCodeListTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "commonCodeList";
	private static final int PRECEDENCE = 10000;

    public CommonCodeListTagProcessor(final String dialectPrefix) {
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
		// 속성값을 타임리프 표준 표현식으로 파싱한 후 표현식을 처리한다.
		Object attrVal;
		try {
			final IEngineConfiguration configuration = context.getConfiguration();
			final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
			final IStandardExpression expression = parser.parseExpression(context, attributeValue);
			attrVal = expression.execute(context);
		} catch (TemplateProcessingException e) {
			log.error(e.getMessage());
			attrVal = attributeValue;
		}

		final String upCdVal = tag.getAttributeValue("upCdVal");

		List<Map<String, String>> codeList = new ArrayList<>();
		try {
			if (StringUtils.isNotBlank(attrVal.toString())) {
				// 공통코드
				List<CmnCdQryDTO> list;
				if (StringUtils.isNotBlank(upCdVal)) {
					list = CommonCodeHelper.getListCmnCd(attrVal.toString(), upCdVal);
				} else {
					list = CommonCodeHelper.getListCmnCd(attrVal.toString());
				}
				if (list != null && !list.isEmpty()) {
					for (CmnCdQryDTO cmnCdQryDTO : list) {
						Map<String, String> codeMap = new LinkedHashMap<>();
						codeMap.put("value", cmnCdQryDTO.getCdVal());
						codeMap.put("text", cmnCdQryDTO.getCdNm());
						codeMap.put("upCdVal", cmnCdQryDTO.getUpCdVal());
						codeList.add(codeMap);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

        StringBuilder result = new StringBuilder();
		result.append( new Gson().toJson(codeList) );

        // 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

}
