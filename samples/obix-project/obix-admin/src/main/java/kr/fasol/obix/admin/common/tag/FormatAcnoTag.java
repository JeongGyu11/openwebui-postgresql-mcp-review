package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 :
 * 설명 : 계좌번호
 * <p/>
 * <pre>
 *     <u:formatAcno value="1234121234567"/> => 1234-12-1234567
 * </pre>
 * - 보안 가이드에는 마스킹 처리가 권고 되어 있으나, 협의 끝에 안하기로 함
 * - 유지보수를 위해 마스킹 기능만 남겨놓음
 * @Class   FormatAcnoTag.class
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
public class FormatAcnoTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String value;


    private @Setter String bnkCode;
    private @Setter boolean mask;

    public FormatAcnoTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

			// 14 자리 문자열만 가능하도록 잡아 둠
			try {
				if (StringUtils.isBlank(bnkCode) || "000".equals(bnkCode)) {
					result = FormatUtil.formatAcno(value, mask);
				} else {
					result = FormatUtil.formatAcno(StringUtils.trimToEmpty(bnkCode), value, mask);
				}
			} catch (IllegalArgumentException e) {
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