package kr.fasol.obix.front.common.thymeleaf.processor;

import java.math.BigInteger;

import kr.fasol.obix.core.util.FormatUtil;
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
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > 금액 포맷팅
 * 프로그램명 : 금액 포맷팅
 * 설명 : 
 *
 * 클래스 : FormatMoneyTagProcessor.java
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
public final class FormatMoneyTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "formatMoney";
	private static final int PRECEDENCE = 10000;

    public FormatMoneyTagProcessor(final String dialectPrefix) {
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

		// 입력 타입에 따른 입력 변수 구분
		// 해당 입력값은 다양한 타입에서 데이터를 받아서 이를 하나의 포맷으로 출력해줌
		BigInteger money = null;

		if (attrVal == null) {
			money = null;
		} else if (attrVal instanceof BigInteger) {
			money = (BigInteger) attrVal;
		} else if (attrVal instanceof Number) {
			money = BigInteger.valueOf(((Number) attrVal).longValue());
		} else if (attrVal instanceof CharSequence) {
			money = new BigInteger(String.valueOf(attrVal));
		} else {
			throw new IllegalArgumentException("허용하지 않는 타입입니다.");
		}

		if (money != null) {
			// FormatUtil의 formatMoney 그대로 사용함
			result.append( FormatUtil.formatMoney(money.toString()) );
		}

        // 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

}
