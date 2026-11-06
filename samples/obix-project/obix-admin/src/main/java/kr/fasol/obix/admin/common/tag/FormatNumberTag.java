package kr.fasol.obix.admin.common.tag;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 시간 포멧변경 출력
 * 설명 :
 *
 * @Class  FormatNumberTag.class
 * @author  정영탁
 * @since   2017-07-26
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-26        정영탁    최초생성
 */
@Slf4j
public class FormatNumberTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private Object value;
    private String pattern;

    public FormatNumberTag() {
        this.var = null;
        this.scope = 1;
        this.pattern = "#,##0";
    }

    @Override
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
        	String result = null;
        	if (value != null && value instanceof BigDecimal) {
        		result = "" + ((BigDecimal) value).longValue();
        	} else {
        		result = (String) value;
        	}

            if (result != null && !result.isEmpty()) {
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                double lAmt = Double.parseDouble(result.trim());
                result = decimalFormat.format(lAmt);
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

        } catch(Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    public void setVar(String var) {
        this.var = var;
    }
    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

}
