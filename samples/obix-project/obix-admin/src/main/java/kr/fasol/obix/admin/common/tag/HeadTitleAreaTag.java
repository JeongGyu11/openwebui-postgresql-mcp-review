package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.EnvironmentUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 타이틀(head.title) 출력
 * 설명 :
 *
 * @Class  HeadTitleAreaTag.class
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
public class HeadTitleAreaTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;

    public HeadTitleAreaTag() {
        this.var = null;
        this.scope = 1;
    }

	@Override
	@SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
			StringBuilder result = new StringBuilder();

            /*
             * 메뉴 목록에서 Location을 추출
             */
			final String headTitle = EnvironmentUtil.getProperty("biz.ui.head.title","");

			if (StringUtils.isNotBlank(headTitle)) {
				result.append(headTitle);
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
                this.pageContext.setAttribute(this.var, result.toString(), this.scope);
            } else {
                out.append(result.toString());
            }

        } catch(Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setVar(String var) {
        this.var = var;
    }
    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

}
