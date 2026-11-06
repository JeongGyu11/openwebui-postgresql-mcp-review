package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.DateUtil;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 현재시간 출력
 * 설명 : 현재시간 출력
 * 사용법
 * <p/>
 * <pre>
 * <u:currentDateTime/>
 * </pre>
 * <p/>
 *
 * @Class  CurrentDateTimeTag.class
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
public class CurrentDateTimeTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String pattern;

    public CurrentDateTimeTag() {
        this.var = null;
        this.scope = 1;
        this.pattern = DateUtil.FORMATTER_YYYY_MM_DD_HH_MM_SS; //"yyyy.MM.dd HH:mm:ss";
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            result = DateUtil.getCurrentDateTime( pattern );

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
                out.append((String)result);
            }

        } catch(Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
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
