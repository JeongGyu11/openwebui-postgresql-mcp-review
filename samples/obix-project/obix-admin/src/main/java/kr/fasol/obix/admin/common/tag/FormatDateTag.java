package kr.fasol.obix.admin.common.tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Custom Tag > 날짜 포멧변경 출력
 * 설명 :
 *
 * ex)
 * <u:formatDate infmt="yyyyMMdd" outfmt="yyyy.MM.dd" value="20071012"/>
 *
 * <b>Attributes</b>
 * - infmt        : 입력 포멧(기본값: yyyyMMdd)
 * - outfmt       : 출력 포멧(기본값: yyyy.MM.dd)
 * - value        : 입력 값
 *
 *
 * @Class  FormatDateTag.class
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
public class FormatDateTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private Object value;
    private String infmt;
    private String outfmt;

    public FormatDateTag() {
        this.var = null;
        this.scope = 1;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

		try {
			String result = "";

			String inputFmt = StringUtils.defaultIfBlank(this.infmt, "yyyyMMdd");
			String outputFmt = StringUtils.defaultIfBlank(this.outfmt, "yyyy.MM.dd");

			/*
			 * 입력값을 outfmt의 형식으로 변환
			 */
			if (this.value == null) {
				result = "";
			} else if (this.value instanceof Date) {
				result = DateUtil.getString((Date) this.value, outputFmt);
			} else {
				result = (String) this.value;

				if (StringUtils.isBlank(result)) {
					result = StringUtils.EMPTY;
				} else {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(inputFmt);
						Date d = sdf.parse(result);
						sdf.applyPattern(outputFmt);
						result = sdf.format(d);
					} catch (ParseException e) {
						result = (String) this.value;
					}
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
                out.append(result);
            }

        } catch(Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void setInfmt(String infmt) {
        this.infmt = infmt;
    }
    public void setOutfmt(String outfmt) {
        this.outfmt = outfmt;
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
