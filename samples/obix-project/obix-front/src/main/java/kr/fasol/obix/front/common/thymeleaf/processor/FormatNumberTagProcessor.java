package kr.fasol.obix.front.common.thymeleaf.processor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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

import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > 시간 포멧변경 출력
 * 프로그램명 : 시간 포멧변경 출력
 * 설명 : 
 *
 * 클래스 : FormatNumberTagProcessor.java
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
public final class FormatNumberTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "formatNumber";
	private static final int PRECEDENCE = 10000;

    public FormatNumberTagProcessor(final String dialectPrefix) {
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

        StringBuilder result = new StringBuilder();

        Double number = null;

        if (attrVal == null) {
        	number = null;
        } else if (attrVal instanceof BigDecimal) {
        	number = ((BigDecimal)attrVal).doubleValue();
        } else if (attrVal instanceof Long) {
        	number = Double.longBitsToDouble((long) attrVal);
		} else {
			throw new IllegalArgumentException("허용하지 않는 타입입니다.");
        }

        if (number != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
            result.append( decimalFormat.format(number) );
        }

        // 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

}
