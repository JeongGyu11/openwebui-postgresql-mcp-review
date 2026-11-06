package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Util > XSS 처리
 * 프로그램명 : Cross Site Scripting (XSS) 방지용 XSS 처리
 * 설명 : 입력값에서 XSS 공격을 방지하기 위해 escape 해주는 로직
 *
 * 클래스 : XssUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 11. 15.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 11. 15.         정영탁          최초생성
 */
@UtilityClass
public class XssUtil {

    /**
     * Html ecsape 문자열 변환
     */
    public String escape(String input) {
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        } else {
            return HtmlUtils.htmlEscape(HtmlUtils.htmlUnescape(input), "UTF-8");
        }
    }

    /**
     * Html ecsape 문자열 변환
     */
    public String unescape(String input) {
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        } else {
            return HtmlUtils.htmlUnescape(input);
        }
    }

    public String escapeXss(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String output = input;
        output = StringUtils.replace(output, "<", "&lt;");
        output = StringUtils.replace(output, ">", "&gt;");
        output = StringUtils.replace(output, "\"", "&quot;");
        output = StringUtils.replace(output, "'", "&apos;");

        return output;
    }

    public String unescapeXss(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String output = input;
        output = StringUtils.replace(output, "&lt;", "<");
        output = StringUtils.replace(output, "&gt;", ">");
        output = StringUtils.replace(output, "&quot;", "\"");
        output = StringUtils.replace(output, "&apos;", "'");

        return output;
    }

}
