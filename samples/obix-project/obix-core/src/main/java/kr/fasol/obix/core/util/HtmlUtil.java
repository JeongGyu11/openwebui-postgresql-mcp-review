package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > HTMl Tag 관련 Util
 * 프로그램명 : HTMl Tag 관련 Util
 * 설명 : 
 *
 * @Class  HtmlUtil.class
 * @author  정영탁
 * @since   2017-07-26
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-26        정영탁    최초생성
 */
@UtilityClass
public class HtmlUtil {

    private static final int UTF8_LENGTH_INFO_BYTE_LENGTH_ID = 0;
    private static final int UTF8_LENGTH_INFO_CURRENT_BYTE_ID = 1;
    private static final int UTF8_LENGTH_INFO_DISPLAY_LENGTH = 2;

    private static String getRegexTag(String tag) {
        return "<" + tag + "[^>]*>|</" + tag + "[^>]*>";
    }

    private static String getRegexTagInnerBody(String tag) {
        return "<" + tag + "[^>]*>.*</" + tag + "[^>]*>";
    }

    private static String removePattern(String text, String regex) {

        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.replaceAll("");
        }

        return text;
    }

    /**
     * HTML 문자열의 특정 태그를 제거한 문자열 반환
     * @param text HTML 형식의 문자열
     * @param tag 제거할 태그명
     * @param removeBody Body 포함 여부(True: 태그 내의 문자열 포함하여 제거, False: 태그만 제거)
     */
    public static String removeTag(String text, String tag, boolean removeBody) {

        String result = text;

        if (removeBody) {
            result = removePattern(result, getRegexTagInnerBody(tag));
        }

        return removePattern(result, getRegexTag(tag));
    }

    /**
     * 해당 문자가 개행문자(\n)인경우 <br />을 추가하여 반환. (\r)제거
     **/
    private static String lineBreakConvertHtml(char ch) {

        String result = null;

        switch (ch) {
            case '\n': {
                result = "<br />\n";
                break;
            }
            case '\r': {
                result = "";
                break;
            }
            default: {
                result = String.valueOf(ch);
                break;
            }
        }

        return result;
    }

    /**
     * Html 특수문자를 Html 엔터티로 변환환다.<br />
     * Html 소스를 웹브라우저에 표시하기 위해 변환합니다.
     *
     * @param text 입력 문자열
     * @param innerLineBreak true시 개행부분에 &lt;br /&gt;를 붙여줍니다.
     */
    public static String toText(String text, boolean innerLineBreak) {

        if (StringUtils.isEmpty(text)) {
            return "";
        }

        String result = text;
        result = result.replace("<", "&lt;");
        result = result.replace(">", "&gt;");
        result = result.replace(" ", "&nbsp;");
        result = result.replace("'", "&#39");
        result = result.replace("\"", "&#34");

        return result;
    }

    /**
     * Text의 개행에 Html태그 &ltbr /&gt를 추가하여 반환한다.<br />
     * Text를 웹브라우저에 표기할때 개행이 되어 표시하기 위해 변환합니다.
     *
     * @param text 입력 문자열
     */
    public static String toInnerLineBreak(String text) {

        if (StringUtils.isEmpty(text)) {
            return "";
        }

        StringBuffer sBuf = new StringBuffer();

        int length = text.length();
        for (int i = 0; i < length; i++) {

            char tmp = text.charAt(i);
            sBuf.append(lineBreakConvertHtml(tmp));
        }

        return sBuf.toString();
    }

    /**
     * HTML 태그를 제거한 문자열을 반환한다.
     */
    public static String cutStringTag(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        String textWithoutTag = str.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>", "");
        textWithoutTag = textWithoutTag.replace('\r', ' ').replace('\n', ' ').replace("&nbsp;", " ").replace("\\s*", " ").trim();
        return textWithoutTag;
    }

    /**
     * HTML 태그를 제거하고, 해당 필드의 보여주는 값이 필드 길이보다 클 때 특정 길이만 보여주고,
     * 나머지는 endStr로 처리한다.
     */
    public static String cutStringTag(String str, int maxLen, String endStr) {
        String textWithoutTag = cutStringTag(str);
        if (textWithoutTag.length() > maxLen) {
            textWithoutTag = textWithoutTag.substring(0, maxLen) + endStr;
        }
        return textWithoutTag;
    }

    /**
     * UTF-8 인코딩 된 문자열 길이 반환
     */
    private static int[] getUTF8LengthInfo(String str, int size, int utf8DisplaySize) {
        int byteLength = 0;
        int currentByte = 0;
        int displayLength = 0;
        byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);

        while (byteLength < byteArray.length) {
            byte tmp = byteArray[byteLength];

            if (tmp == 9 || tmp == 10 || (tmp >= 32 && tmp <= 126)) {
                byteLength++;
                currentByte = 1;
                displayLength++;
            } else if (tmp >= -62 && tmp <= -33) { // tmp >= 194 && tmp <= 223
                byteLength += 2;
                currentByte = 2;
                displayLength += utf8DisplaySize;
            } else if (tmp >= -32 && tmp <= -17) { // tmp >= 224 && tmp <= 239
                byteLength += 3;
                currentByte = 3;
                displayLength += utf8DisplaySize;
            } else if (tmp >= -16 && tmp <= -9) { // tmp >= 240 && tmp <= 247
                byteLength += 4;
                currentByte = 4;
                displayLength += utf8DisplaySize;
            } else if (tmp >= -8 && tmp <= -5) { // tmp >= 248 && tmp <= 251
                byteLength += 5;
                currentByte = 5;
                displayLength += utf8DisplaySize;
            } else if (tmp >= -4 && tmp <= -3) { // tmp >= 252 && tmp <= 253
                byteLength += 6;
                currentByte = 6;
                displayLength += utf8DisplaySize;
            } else {
                byteLength++;
            }

            if (size > 0 && displayLength >= size) {
                break;
            }
        }

        int[] lengthInfoArray = new int[3];
        lengthInfoArray[UTF8_LENGTH_INFO_BYTE_LENGTH_ID] = byteLength;
        lengthInfoArray[UTF8_LENGTH_INFO_CURRENT_BYTE_ID] = currentByte;
        lengthInfoArray[UTF8_LENGTH_INFO_DISPLAY_LENGTH] = displayLength;

        byteArray = null;

        return lengthInfoArray;
    }

    /**
     * UTF-8 인코딩한 문자열로 반환
     */
    public static String UTF8Encoding(String str) {
        int[] lengthInfoArray = getUTF8LengthInfo(str, str.length(), 2);
        int byteLength = lengthInfoArray[UTF8_LENGTH_INFO_BYTE_LENGTH_ID];

        byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);
        byte[] newByte = new byte[byteLength];
        System.arraycopy(byteArray, 0, newByte, 0, newByte.length);

        String resultStr = new String(newByte, StandardCharsets.UTF_8);

        byteArray = null;
        newByte = null;

        return resultStr;
    }

    /**
     * UTF-8 인코딩한 문자열의 substring 반환
     */
    public static String UTF8Substring(String str, int size, int utf8DisplaySize, String endStr) {
        int[] lengthInfoArray = getUTF8LengthInfo(str, size, utf8DisplaySize);
        int byteLength = lengthInfoArray[UTF8_LENGTH_INFO_BYTE_LENGTH_ID];
        int currentByte = lengthInfoArray[UTF8_LENGTH_INFO_CURRENT_BYTE_ID];
        int displayLength = lengthInfoArray[UTF8_LENGTH_INFO_DISPLAY_LENGTH];

        if (displayLength > size) {
            byteLength -= currentByte;
        }

        byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);
        byte[] newByte = new byte[byteLength];
        System.arraycopy(byteArray, 0, newByte, 0, newByte.length);

        String resultStr = new String(newByte, StandardCharsets.UTF_8);
        if (byteLength < byteArray.length) {
            resultStr += endStr;
        }

        byteArray = null;
        newByte = null;

        return resultStr;
    }

    /**
     * 2 bytes 문자열의 substring 반환
     */
    public static String twoByteSubstring(String str, int size, String endStr, String encoding) throws Exception {
        byte[] byteArray = str.getBytes(encoding);

        if (size < byteArray.length) {
            int twoByteChar = 0;
            for (int i = 0; i < size; i++) {
                if (byteArray[i] < 0) {
                    twoByteChar++;
                }
            }

            int finalSize = size;
            if (twoByteChar % 2 == 1) {
                finalSize--;
            }

            byte[] newByte = new byte[finalSize];
            System.arraycopy(byteArray, 0, newByte, 0, newByte.length);

            return new String(newByte, encoding) + endStr;
        } else {
            return str;
        }
    }

    /**
     * 한글의substring 반환
     */
    public static String korSubstring(String str, int size, String endStr) {
        int count = 0;
        int i = 0;
        for (i = 0; i < str.length(); i++) {
            int codeval = str.codePointAt(i);
            if (codeval < 128) {
                count++;
            } else {
                count += 2;
            }

            if (count > size) {
                break;
            }
        }

        String resultStr = str.substring(0, i);
        if (i < str.length()) {
            resultStr += endStr;
        }

        return resultStr;
    }

}
