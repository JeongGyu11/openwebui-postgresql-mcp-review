package kr.fasol.obix.admin.common.tag;

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
 * 세부업무구분명 : 커스텀 태그 > 공통 > 사업자 등록 번호 포맷팅 출력
 * 설명 :
 *
 * @Class   FormatBisnoTag.class
 * @author  정영탁
 * @since   2017-08-04
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일        수정자                수정내용
 *--------------------------------------------------------------------
 * 2017-08-04   정영탁  최초생성
 *
 */
@Slf4j
public class FormatBisnoTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String value;

    public FormatBisnoTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            // 10 자리 문자열만 가능하도록 잡아 둠
            try{
                result = FormatUtil.formatBisno( value );
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

        } catch(Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void setVar(String var) {
        this.var = var;
    }
    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

}