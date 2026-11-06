package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.FormatUtil;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 :
 * 설명 : 주민번호 포맷팅
 * - 보안 개발 가이드에 따라 마스킹을 기본적으로 처리 했음
 * <p/>
 * <pre>
 *     <u:formatRnno value="1234561234567"/><p/>    => 123456-*******
 *     <u:formatRnno value="1234561234567" mask="false"/> => 123456-1234567
 * </pre>
 * @Class   FormatRnnoTag.class
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
public class FormatRnnoTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String value;

    private @Setter boolean mask;

    public FormatRnnoTag() {
        this.var = null;
        this.scope = 1;
        this.mask = true;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            try{
                result = FormatUtil.formatRnno( value, mask );
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
