package kr.fasol.obix.admin.common.tag;

import java.math.BigDecimal;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.FormatUtil;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 :
 * 설명 :
 *
 * @Class   FormatInterestTag.class
 * @author  정영탁
 * @since   2017-08-10
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일        수정자                수정내용
 *--------------------------------------------------------------------
 * 2017-08-10   정영탁  최초생성
 *
 */
@Slf4j
public class FormatInterestTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private Object value;

    public FormatInterestTag() {
        this.var = null;
        this.scope = 1;

    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";
            BigDecimal interest = null;

            // 입력 타입에 따른 입력 변수 구분
            // 해당 입력값은 다양한 타입에서 데이터를 받아서 이를 하나의 포맷으로 출력해줌

            if(value == null ){
                interest = null;
            } else if (value instanceof BigDecimal) {
                interest = (BigDecimal) value;
            } else if (value instanceof Number) {
                interest = BigDecimal.valueOf(((Number) value).floatValue());
            } else if (value instanceof CharSequence) {
                interest = new BigDecimal(String.valueOf(value));
            } else {
                // TODO: 2017-08-07 User Exception
                throw new IllegalArgumentException("허용하지 않는 타입입니다.");
            }

            // FormatUtil의 formatMoney 그대로 사용함
            try{
                if ( interest == null ) result = "";
                else result = FormatUtil.formatInterest(interest.toString());
            } catch (IllegalArgumentException e )
            {
                log.error(e.getMessage());
            }
            /*
             * Output에 결과 전송
             */
            if (this.var != null) {
                if (this.scope == 1) {
                    VariableMapper vm = this.pageContext.getELContext().getVariableMapper();
                    if (vm != null) {
                        vm.setVariable(this.var, null);
                    }
                }
                this.pageContext.setAttribute(this.var, result, this.scope);
            } else {
                out.append(result);
            }

        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }
}
