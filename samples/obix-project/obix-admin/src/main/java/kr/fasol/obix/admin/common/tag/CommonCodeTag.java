package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import kr.fasol.obix.admin.common.util.CommonCodeHelper;
import kr.fasol.obix.admin.dto.comm.CmnCdQryDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 공통코드 정보
 * 설명 :
 *
 * @Class  CommonCodeTag.class
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
public class CommonCodeTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;

    private @Setter String cdGroupVal;
    private @Setter String upCdVal;
    private @Setter String cdVal;

    public CommonCodeTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            if(StringUtils.isNotBlank(cdGroupVal) && StringUtils.isNotBlank(cdVal)) {
        		// 공통코드
            	CmnCdQryDTO cmnCdQryDTO;
            	if (StringUtils.isNotBlank(upCdVal)) {
                	cmnCdQryDTO = CommonCodeHelper.getCmnCd(cdGroupVal, upCdVal, cdVal);
            	} else {
                	cmnCdQryDTO = CommonCodeHelper.getCmnCd(cdGroupVal, cdVal);
            	}
            	if (cmnCdQryDTO != null) {
            		result = cmnCdQryDTO.getCdNm();
            	}
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
                out.append((String)result);
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
