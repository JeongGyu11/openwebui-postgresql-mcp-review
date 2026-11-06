package kr.fasol.obix.admin.common.tag;

import java.math.BigInteger;

import jakarta.el.VariableMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import kr.fasol.obix.core.util.FormatUtil;
import org.apache.jasper.tagplugins.jstl.Util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > 커스텀 태그 > 금액 포맷팅
 * 설명 : 금액 포맷팅
 * <p/>
 * <pre>
 *     <u:formatMoney value="12345678901234567890"/><p/>
 *     <u:formatMoney value="${money}"/><p/>
 *     <u:formatMoney value="${money2}"/><p/>
 * </pre>
 * @Class   FormatMoneyTag.class
 * @author  정영탁
 * @since   2017-08-07
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일        수정자                수정내용
 *--------------------------------------------------------------------
 * 2017-08-07   정영탁  최초생성
 *
 */
@Slf4j
public class FormatMoneyTag extends TagSupport {

    private HttpServletRequest request;
    private String var;
    private int scope;
    private Object value;

    private @Setter boolean comma;
    private @Setter int position;

    public FormatMoneyTag() {
        this.var = null;
        this.scope = 1;
        this.comma = true;
        this.position = 0;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        request = (HttpServletRequest) pageContext.getRequest();

        try {
            String result = "";
            BigInteger money  = null;

            // 입력 타입에 따른 입력 변수 구분
            // 해당 입력값은 다양한 타입에서 데이터를 받아서 이를 하나의 포맷으로 출력해줌

            if(value == null )
            {
                money = null;
            }
            else if( value instanceof BigInteger )
            {
                money = (BigInteger) value;
            }
            else if( value instanceof Number ) {
                money = BigInteger.valueOf( ((Number) value).longValue() );
            }
            else if( value instanceof CharSequence )
            {
                money = new BigInteger( String.valueOf(value));
            }
            else {
                // TODO: 2017-08-07 User Exception
                throw new IllegalArgumentException("허용하지 않는 타입입니다.");
            }

            try{
                if( money == null ) result = "";
                else {
                    // FormatUtil의 formatMoney 그대로 사용함
                    result = FormatUtil.formatMoney(money.toString(10), comma, position);
                }
            } catch (IllegalArgumentException e )
            {
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

        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
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
