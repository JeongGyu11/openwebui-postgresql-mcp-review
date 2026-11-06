package kr.fasol.obix.admin.common.tag;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 네비게이션(Line Map) 출력
 * 설명 :
 *
 * @Class  LocationAreaTag.class
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
public class LocationAreaTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;

    public LocationAreaTag() {
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

            List<AdmScrenTranBasQryDTO> locationList = new ArrayList<>();
            String pathWithinHandlerMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

            /*
             * 메뉴 목록에서 Location을 추출
             */
            locationList = FunctionUtil.getMenuList(Objects.requireNonNullElse(AdminSessionHelper.getAdmScrenMenuList(), new ArrayList<>()), pathWithinHandlerMapping);

            if (!locationList.isEmpty()) {
                String urlAddr;
                for (int i = locationList.size() - 1; i >= 0; i--) {
                    urlAddr = locationList.get(i).getUrlAddr();
                    result.append("<li>");
                    if (StringUtils.isNotBlank(urlAddr)) {
                        if (urlAddr.startsWith("http://") || urlAddr.startsWith("https://")) {
                            result.append("<a href=\"").append(urlAddr).append("\" target=\"_blank\">");
                            result.append(locationList.get(i).getScrenTranNm());
                            result.append("</a>");
                        } else if (urlAddr.startsWith("javascript:")) {
                            result.append("<a href=\"#none\" onclick=\"").append(urlAddr).append(";return false;\">");
                            result.append(locationList.get(i).getScrenTranNm());
                            result.append("</a>");
                        } else {
                            result.append("<a href=\"#none\" onclick=\"navigate('").append(urlAddr).append("');return false;\">");
                            result.append(locationList.get(i).getScrenTranNm());
                            result.append("</a>");
                        }
                    } else {
                        result.append(locationList.get(i).getScrenTranNm());
                    }
                    result.append("</li>");
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
