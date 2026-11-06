package kr.fasol.obix.front.common.thymeleaf.processor;

import kr.fasol.obix.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > 날짜 포멧팅
 * 프로그램명 : 날짜 포멧팅
 * 설명 : 
 * <pre>
 * <html xmlns:obix="http://obix.fasol.co.kr/thymeleaf/layout">
 * 
 * <th:block obix:currentDate="yyyy.MM.dd"/>
 * </pre>
 *
 * 클래스 : CurrentDateTagProcessor.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 4.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 4.        정영탁			  최초작성
 */
public final class CurrentDateTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "currentDate";
	private static final int PRECEDENCE = 10000;

    public CurrentDateTagProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML		// HTML 모드로 설정
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

		String pattern = DateUtil.FORMATTER_YYYY_MM_DD; //"yyyy.MM.dd";
		if (StringUtils.isNotBlank(attributeValue)) {
			pattern = attributeValue;
		}

        StringBuilder result = new StringBuilder();
        result.append( DateUtil.getCurrentDate( pattern ) );

        // 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

}
