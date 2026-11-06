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

import java.util.ArrayList;
import java.util.List;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > 표준코드 Select Form
 * 프로그램명 : 표준코드 Select Form
 * 설명 :
 * <pre>
 * <html xmlns:obix="http://obix.fasol.co.kr/thymeleaf/layout">
 *
 * <select obix:formSelectStndCode="10" th:attr="cdGroupVal='ADM_USER_STUS_CD', upCdVal='', allOption='선택'"></select>
 * </pre>
 *
 * 클래스 : FormSelectStndCodeTagProcessor.java
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
public class FormSelectStndCodeTagProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTR_NAME = "formSelectStndCode";
    private static final int PRECEDENCE = 10000;

    public FormSelectStndCodeTagProcessor(final String dialectPrefix) {
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

        final String cdGroupVal = tag.getAttributeValue("cdGroupVal");
        final String upCdVal = tag.getAttributeValue("upCdVal");
        final String allOption = tag.getAttributeValue("allOption");
        final String excludeCdVal = tag.getAttributeValue("excludeCdVal");

        StringBuilder result = new StringBuilder();
        try {
            if (StringUtils.isNotBlank(cdGroupVal)) {
                // 공통코드
                List<CmnCdQryDTO> list;
                if (StringUtils.isNotBlank(upCdVal)) {
                    list = CommonCodeHelper.getListCmnCd(cdGroupVal, upCdVal);
                } else {
                    list = CommonCodeHelper.getListCmnCd(cdGroupVal);
                }
                if( StringUtils.isNotBlank(allOption)) {
                    result.append(String.format("<option value=\"\">%s</option>", allOption));
                }
                if (list != null && !list.isEmpty()) {
                    for (CmnCdQryDTO cmnCdQryDTO : list) {
                        String _cdVal = cmnCdQryDTO.getCdVal();
                        String _cdNm = cmnCdQryDTO.getCdNm();
                        String _upCdVal = StringUtils.trimToEmpty(cmnCdQryDTO.getUpCdVal());

                        if (StringUtils.isNotBlank(excludeCdVal)) {
                            if (excludeCdVal.contains(_cdVal)) {
                                continue;
                            }
                        }

                        String selectedStr = (StringUtils.isNotBlank(attrVal.toString()) && _cdVal.equals(attrVal.toString()))? "selected=\"selected\"": "";
                        result.append(String.format("<option value=\"%s\" upCdVal=\"%s\" %s>%s</option>", _cdVal, _upCdVal, selectedStr, _cdNm));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 현재 요소에 본문 설정
        structureHandler.setBody(result, false);

    }

}
