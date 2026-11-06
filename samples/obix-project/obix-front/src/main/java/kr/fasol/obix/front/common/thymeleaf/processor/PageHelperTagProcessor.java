package kr.fasol.obix.front.common.thymeleaf.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.fasol.obix.core.base.IPageInfo;
import kr.fasol.obix.core.util.JsonUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > Page Navigation 출력
 * 프로그램명 : Page Navigation 출력
 * 설명 :
 * ex)
 *   <span obix:pageHelper="${output.pageInfo}" th:attr="urlFormat=\"javascript:lfGoPage('%s');\""></span>
 *
 * 클래스 : PageHelperTagProcessor.java
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
public final class PageHelperTagProcessor extends AbstractAttributeTagProcessor {

	private static final String ATTR_NAME = "pageHelper";
	private static final int PRECEDENCE = 10000;

    public PageHelperTagProcessor(final String dialectPrefix) {
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
	@SuppressWarnings("unchecked")
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

		final String urlFormat = tag.getAttributeValue("urlFormat");

		Map<String, Object> map;
		if (attrVal instanceof IPageInfo) {
			map = JsonUtil.convertValue(attrVal, Map.class);
		} else {
			map = (Map<String, Object>) attrVal;
		}
		if (map == null) {
			map = new HashMap<>();
		}
		map.put("urlFormat", urlFormat);

		String result = generate(map);

        // 현재 요소에 본문 설정
		structureHandler.setBody(result, false);

	}

	/**
	 * HTML 결과 생성
	 */
	@SuppressWarnings("unchecked")
	private String generate(Map<String, Object> map) {
		StringBuilder outputHtml = new StringBuilder();
		String urlFormat = (String)map.get("urlFormat");
		int pageNo = (int)map.get("pageNo");
		int totalCount = (int)map.get("totalCount");
		//int pageSize = (int)map.get("pageSize");
		//int pageBlock = (int)map.get("pageBlock");
		int totalPage = (int)map.get("totalPage");
		//int startPage = (int)map.get("startPage");
		//int endPage = (int)map.get("endPage");
		List<Integer> pages = (List<Integer>)map.get("pages");
		int firstPage = (int)map.get("firstPage");
		int prevPage = (int)map.get("prevPage");
		int nextPage = (int)map.get("nextPage");
		int lastPage = (int)map.get("lastPage");

		if (totalCount > 0) {
			if (pageNo > 1) {
				outputHtml.append("<li class=\"first\"><a href=\"").append(String.format(urlFormat, firstPage)).append("\">처음</a></li>\n");
				outputHtml.append("<li class=\"prev\"><a href=\"").append(String.format(urlFormat, prevPage)).append("\">이전</a></li>\n");
			}
			for (Integer itemNo : pages) {
				if (itemNo == pageNo) {
					outputHtml.append("<li><strong>").append(itemNo).append("</strong></li>");
				} else {
					outputHtml.append("<li><a href=\"").append(String.format(urlFormat, itemNo)).append("\">").append(itemNo).append("</a></li>\n");
				}
			}
			if (pageNo < totalPage) {
				outputHtml.append("<li class=\"next\"><a href=\"").append(String.format(urlFormat, nextPage)).append("\">다음</a></li>\n");
				outputHtml.append("<li class=\"last\"><a href=\"").append(String.format(urlFormat, lastPage)).append("\">끝</a></li>\n");
			}
		}

		return outputHtml.toString();
	}

}
