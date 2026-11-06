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
 * 세부업무구분명 : 공통 > Custom Tag > 표준코드 Radio Form
 * 설명 :
 *
 * @Class  FormRadioStndCodeTag.class
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
public class FormRadioStndCodeTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private String value;

    private @Setter String cdGroupVal;
    private @Setter String upCdVal;
    private @Setter int col;

    private @Setter String name;
    private @Setter String className;
    private @Setter String style;
    private @Setter String tabindex;

    private @Setter boolean required;
    private @Setter String alias;
    private @Setter boolean disabled;

    public FormRadioStndCodeTag() {
        this.var = null;
        this.scope = 1;
        this.col = -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";

            StringBuilder inputRadioSB = new StringBuilder();

            inputRadioSB.append("<span class=\"box-radio\">");
            // form attr 추가
            List<String> baseAttr = new ArrayList<>();

            if( StringUtils.isNotBlank(name) )      baseAttr.add(String.format("name=\"%s\"", name));
            if( StringUtils.isNotBlank(className) ) baseAttr.add(String.format(" class=\"%s\"", className));
            if( StringUtils.isNotBlank(style) )     baseAttr.add(String.format("style=\"%s\"", style));
            if( StringUtils.isNotBlank(tabindex))   baseAttr.add(String.format("tabindex=\"%s\"", style));
            if( disabled )   baseAttr.add("disabled");

            if(StringUtils.isNotBlank(cdGroupVal)) {

        		// 공통코드
        		List<CmnCdQryDTO> list;
        		if (StringUtils.isNotBlank(upCdVal)) {
        			list = CommonCodeHelper.getListCmnCd(cdGroupVal, upCdVal);
        		} else {
        			list = CommonCodeHelper.getListCmnCd(cdGroupVal);
        		}
        		if (null != list  && !list.isEmpty()) {
        			String templateTag = "<input type='radio' id='%s' value='%s' upCdVal='%s' %s/><label for='%s'>%s</label>";
        			for (int i = 0; i < list.size(); i++) {
        				String cdVal = list.get(i).getCdVal();
        				String cdNm = list.get(i).getCdNm();
        				String upCdVal = StringUtils.trimToEmpty(list.get(i).getUpCdVal());
        				
        				List<String> attr = new ArrayList<>(baseAttr);
        				String id = String.format("%s_%s", name, cdVal );
        				if(StringUtils.isNotBlank(value) &&  cdVal.equals(value)){
        					attr.add("checked");
        				}
        				if(i==0) {
        					if( required )                      attr.add("data-validate-rule=\"{required:true}\"");
        					if( StringUtils.isNotBlank(alias) ) attr.add(String.format("data-alias=\"%s\"", alias));
        				}
        				
        				String attrStr = String.join(" ", attr);
        				inputRadioSB.append(String.format(templateTag, id, cdVal, upCdVal, attrStr, id, cdNm));
        				if( col > 0 && 0 == (i+1) % col )inputRadioSB.append("<br/>");
        			}
        		}
            }

            inputRadioSB.append("</span>");
            result = inputRadioSB.toString();

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
