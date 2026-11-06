package kr.fasol.obix.admin.common.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.fasol.obix.core.base.IPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.jasper.tagplugins.jstl.Util;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > Page Navigation 출력
 * 설명 :
 *
 * ex)
 * <u:pageHelper urlFormat="javascript:lfGoPage('%s');" value="${output.pageInfo}" />
 *
 *
 * @Class  PageHelperTag.class
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
public class PageHelperTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private Object value;
    private String urlFormat;

    public PageHelperTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
        	Map<String, Object> map;
        	if (this.value instanceof IPageInfo) {
        		map = new ObjectMapper().convertValue(this.value, Map.class);
        	} else {
        		map = (Map<String, Object>) this.value;
        	}
            if (map == null) {
                map = new HashMap<String, Object>();
            }
            map.put("urlFormat", this.urlFormat);

            String result = generate(map);

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

    /**
     * HTML 결과 생성
     */
    @SuppressWarnings("unchecked")
    private String generate(Map<String, Object> map) throws Exception {
        StringBuilder outputHtml = new StringBuilder();
        String urlFormat = (String)map.get("urlFormat");
        int pageNo = (int)map.get("pageNo");
        int totalCount = (int)map.get("totalCount");
        //int pageSize = (int)map.get("pageSize");
        //int pageBlock = (int)map.get("pageBlock");
        int totalPage = (int)map.get("totalPage");
        //int startPage = (int)map.get("startPage");
        //int endPage = (int)map.get("endPage");
        List<Integer> pages = (List<Integer>)map.get("pages");
        int firstPage = (int)map.get("firstPage");
        int prevPage = (int)map.get("prevPage");
        int nextPage = (int)map.get("nextPage");
        int lastPage = (int)map.get("lastPage");

        if (totalCount > 0) {
            if (pageNo > 1) {
                outputHtml.append("<li class=\"first\"><a href=\"").append(String.format(urlFormat, firstPage)).append("\">처음</a></li>\n");
                outputHtml.append("<li class=\"prev\"><a href=\"").append(String.format(urlFormat, prevPage)).append("\">이전</a></li>\n");
            }
            for (Integer itemNo : pages) {
                if (itemNo == pageNo) {
                    outputHtml.append("<li><strong>").append(itemNo).append("</strong></li>");
                } else {
                    outputHtml.append("<li><a href=\"").append(String.format(urlFormat, itemNo)).append("\">").append(itemNo).append("</a></li>\n");
                }
            }
            if (pageNo < totalPage) {
                outputHtml.append("<li class=\"next\"><a href=\"").append(String.format(urlFormat, nextPage)).append("\">다음</a></li>\n");
                outputHtml.append("<li class=\"last\"><a href=\"").append(String.format(urlFormat, lastPage)).append("\">끝</a></li>\n");
            }
        }

        return outputHtml.toString();
    }

    public void setUrlFormat(String urlFormat) {
        this.urlFormat = urlFormat;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public void setVar(String var) {
        this.var = var;
    }
    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

}
