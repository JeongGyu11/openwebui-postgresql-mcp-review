package kr.fasol.obix.admin.common.tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import com.google.gson.Gson;

import kr.fasol.obix.admin.common.util.CommonCodeHelper;
import kr.fasol.obix.admin.dto.comm.CmnCdQryDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 공통코드 목록
 * 설명 :
 *
 * @Class  CommonCodeListTag.class
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
public class CommonCodeListTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;

    private @Setter String cdGroupVal;
    private @Setter String upCdVal;

    public CommonCodeListTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
    		List<Map<String, String>> codeList = new ArrayList<>();

            if(StringUtils.isNotBlank(cdGroupVal)) {
        		// 공통코드
            	List<CmnCdQryDTO> list;
            	if (StringUtils.isNotBlank(upCdVal)) {
            		list = CommonCodeHelper.getListCmnCd(cdGroupVal, upCdVal);
            	} else {
            		list = CommonCodeHelper.getListCmnCd(cdGroupVal);
            	}
            	if (list != null && !list.isEmpty()) {
            		for (int i = 0; i < list.size(); i++) {
            			Map<String, String> codeMap = new LinkedHashMap<>();
            			codeMap.put("value", list.get(i).getCdVal());
            			codeMap.put("text", list.get(i).getCdNm());
            			codeMap.put("upCdVal", list.get(i).getUpCdVal());
            			codeList.add(codeMap);
            		}
            	}
            }

            StringBuilder result = new StringBuilder();
            result.append( new Gson().toJson(codeList) );

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
