package kr.fasol.obix.admin.common.tag;

import java.util.ArrayList;
import java.util.List;

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
 * 세부업무구분명 : 공통 > Custom Tag > 표준코드 Select Form
 * 설명 :
 *
 * @Class  FormSelectStndCodeTag.class
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
public class FormSelectStndCodeTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String value;
    
    private @Setter String cdGroupVal;
    private @Setter String upCdVal;

    private @Setter String id;
    private @Setter String name;
    private @Setter String onchange;
    private @Setter String className;
    private @Setter String style;
    private @Setter String tabindex;
    private @Setter String allOption;
    private @Setter String excludeCdVal;

    private @Setter boolean required;
    private @Setter String alias;

    public FormSelectStndCodeTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            StringBuilder selectBuilder = new StringBuilder();

            // form attr 추가
            List<String> attr = new ArrayList<>();
            if( StringUtils.isNotBlank(id) )        attr.add(String.format("id=\"%s\"", id));
            if( StringUtils.isNotBlank(name) )      attr.add(String.format("name=\"%s\"", name));
            if( StringUtils.isNotBlank(onchange) )  attr.add(String.format("onchange=\"%s\"", onchange));
            if( StringUtils.isNotBlank(className) ) attr.add(String.format(" class=\"%s\"", className));
            if( StringUtils.isNotBlank(style) )     attr.add(String.format("style=\"%s\"", style));
            if( StringUtils.isNotBlank(tabindex) )  attr.add(String.format("tabindex=\"%s\"", tabindex));
            if( required )                          attr.add("data-validate-rule=\"{required:true}\"");
            if( StringUtils.isNotBlank(alias) )     attr.add(String.format("data-alias=\"%s\"", alias));

            selectBuilder.append(String.format("<select %s >", String.join(" ", attr) ));

            if(StringUtils.isNotBlank(cdGroupVal)) {

        		// 공통코드
            	List<CmnCdQryDTO> list;
            	if (StringUtils.isNotBlank(upCdVal)) {
            		list = CommonCodeHelper.getListCmnCd(cdGroupVal, upCdVal);
            	} else {
            		list = CommonCodeHelper.getListCmnCd(cdGroupVal);
            	}
            	if( StringUtils.isNotBlank(allOption)) {
            		selectBuilder.append(String.format("<option value=\"\">%s</option>", allOption));
            	}
            	if (null != list  && !list.isEmpty()) {
            		for (CmnCdQryDTO item  : list) {
            			String cdVal = item.getCdVal();
                        String cdNm = item.getCdNm();
                        String upCdVal = StringUtils.trimToEmpty(item.getUpCdVal());

        				if (StringUtils.isNotBlank(excludeCdVal)) {
        					if (excludeCdVal.contains(cdVal)) {
        						continue;
        					}
        				}

                        String selectedStr = (StringUtils.isNotBlank(value) && cdVal.equals(value))? "selected=\"selected\"": "";
                        selectBuilder.append(String.format("<option value=\"%s\" upCdVal=\"%s\" %s>%s</option>", cdVal, upCdVal, selectedStr, cdNm));
					}
            	}
            }
            
            selectBuilder.append("</select>");
            result = selectBuilder.toString();

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
