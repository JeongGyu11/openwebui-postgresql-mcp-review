package kr.fasol.obix.core.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > Format 관련 Util (형식 변환)
 * 프로그램명 : Format 관련 Util (형식 변환)
 * 설명 :
 *
 * @Class  FormatUtil.class
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
@Slf4j
@UtilityClass
public class FormatUtil {

    /**
     * 날짜 포맷 기본
     */
    public static final DateTimeFormatter FORMATTER_YYYY = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter FORMATTER_DD = DateTimeFormatter.ofPattern("dd");
    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter FORMATTER_HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_HHMMSSSSS = DateTimeFormatter.ofPattern("HHmmssSSS");
    public static final DateTimeFormatter FORMATTER_HHMMSSSS = DateTimeFormatter.ofPattern("HHmmssSS");
    public static final DateTimeFormatter FORMATTER_HHMMSS = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSSSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final NumberFormat FORMATTER_NUMBER_THOUSANDS = NumberFormat.getInstance();

    // 전화번호 포맷
    public static final String FORMATTER_PHONE_NUMBER = "(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-([0-9]{3,4})-([0-9]{4})";

    public static final String DEFAULT_DELIMITER_DATE = ".";
    public static final String DEFAULT_DELIMITER_TIME = ":";
    /**
     * 특수 문자열
     */
    public static final String DEFAUlT_SPECIAL_KEY = "!,*&^%$#@~;|>+";


    /**
     * 원하는 형식에 맞춰 문자열 포맷팅 변환.
     * <p>
     * <pre>
     * "123-45-67890" = formatStringDelimiter("1234567890", "-", new int[]{3,2,5});
     * </pre>
     *
     * @param str        문자열
     * @param delimiter  삽입될 문자열
     * @param num        삽입될 문자열간 길이
     */
    public static String formatStringDelimiter(String str, String delimiter, int[] num) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (null == num || num.length == 1) {
            return str;
        }
        int size = num.length;
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (int i = 0; i < size; i++) {
            stringBuilder.append(str.subSequence(index, index + num[i]));
            if (i != size - 1) {
                stringBuilder.append(delimiter);
            }
            index += num[i];
        }
        return stringBuilder.toString();
    }

    /**
     * 사업자번호, 주민번호 포맷팅.
     * <p>
     * <pre>
     * "123456-1234567" = formatNO("1234561234567", false);
     * "123456-*******" = formatNO("1234561234567", true);
     *
     * "123-45-6789" = formatNO("1234567890", false);
     * "123-45-6789" = formatNO("1234567890", true); (X)
     * </pre>
     *
     * @param no 주민번호 또는 사업자 번호
     */
    public static String formatNO(String no) {
        return formatNO(no, true);
    }
    /**
     * 사업자번호, 주민번호 포맷팅.
     * <p>
     * <pre>
     * "123456-1234567" = formatNO("1234561234567", false);
     * "123456-*******" = formatNO("1234561234567", true);
     *
     * "123-45-67890" = formatNO("1234567890", false);
     * "123-45-67890" = formatNO("1234567890", true); (X)
     * </pre>
     *
     * @param no 주민번호 또는 사업자 번호
     * @param mask 주민번호 뒷자리 마스킹 여부
     */
    public static String formatNO(String no, boolean mask) {

        if (StringUtils.isBlank(no)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(no.trim());
        if (sb.length() == 10) { // 10자리 포멧
            sb.insert(3, "-").insert(6, "-");
        } else if (sb.length() == 13) {
            if (sb.charAt(0) == '0' && sb.charAt(6) == '0' && sb.charAt(7) == '0') { // 13자리 포멧
                sb.deleteCharAt(0);
                sb.deleteCharAt(5);
                sb.deleteCharAt(5);
                sb.insert(3, "-").insert(6, "-");
            } else { // 개인사업자 주민번호
                sb.insert(6, "-");
                if (mask) {
                    sb.replace(7, 14, "*******");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 금액 문자열을 comma 표시 여부와 소수점 표시 여부를 확인하여 format된 문자열로 반환한다.
     * <p>
     * <ui>
     * <li> 금액 문자열이 음수(-)이면 반환된 값에도 음수(-)가 붙는다.</li>
     * <li> 금액 문자열에 소숫점이 있고 point 값이 있다면 둘 중 큰 숫자만큼 소수점 자리가 나온다.</li>
     * <li> 금액 문자열에 소숫점이 있고 point 값이 0이라면 금액 문자열의 소숫점이하가 0이면 절삭처리한다. 하지만 값이 있다면 절삭하지 않는다.</li>
     * </ui>
     * <pre>
     * 	"121,564,899.231" = formatMoney("0121564899.231", true, 0)
     * 	"121,564,899.231" = formatMoney("0121564899.231", true, 2)
     * 	"121564899.231" = formatMoney("0121564899.231", false, 2)
     * 	"121,564,899.2310" = formatMoney("0121564899.231", true, 4)
     * 	"121564899.2310" = formatMoney("0121564899.231", false, 4)
     * 	"-121,564,899.2310" = formatMoney("-0121564899.231", true, 4)
     * 	"-121564899.2310" = formatMoney("-0121564899.231", false, 4)
     * </pre>
     *
     * @param money  금액
     * @param comma  금액 표시시 천단위 구분자(,) 삽입여부
     * @param point  소수점의 자릿수
     */
    public static String formatMoney(String money, boolean comma, int point) {
        String pattern;
        if (comma) {
            pattern = "#,##0";
        } else {
            pattern = "###0";
        }
        int index = money.lastIndexOf('.');
        if (point < 1) {
            pattern += ".";
            index = money.length() - (index + 1);
            pattern += StringUtils.repeat('#', Math.max(index, 1));
        } else {
            pattern += ".";
            if (index < 0) {
                pattern += StringUtils.repeat('0', point);
            } else {
                index = money.length() - (index + 1);
                pattern += StringUtils.repeat('0', NumberUtils.max(index, point, 1));
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        double lAmt = Double.parseDouble(money.trim());
        return decimalFormat.format(lAmt);
    }

    public static String formatMoney( @NonNull String money ) {
        return formatMoney(money, true, 0);
    }


    /**
     * 이자율을 포맷에 맞추어 전달하는 함수
     * 소수점 둘째자리까지 맞추어서 표시함
     * <p>
     * <pre>
     * 	"5.00" = formatMoney("5")
     * 	"12.25" = formatMoney("12.25")
     * </pre>
     * @param interest 이자율
     * @return
     */
    public static String formatInterest(String interest )
    {
        String pattern = "#0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        double lAmt = Double.parseDouble(interest.trim());
        return decimalFormat.format(lAmt);
    }

    /**
     * 숫자에서 콤마 등 특수문자 제거 ( 소숫점, 마이너스기호 제외 )
     * <p>
     * <pre>
     * "112000" = getNumberUnformat("112,000");
     * </pre>
     * @param money 금액
     */
    public static String getNumberUnformat(String money) {
        String result = RegExUtils.removePattern(money, "[^0-9-+.]");
        if ("".equals(result)) {
            result = "0";
        }
        return result;
    }

    /**
     * <pre>
     * 숫자를 제외한 문자 제거
     * </pre>
     * @param str : 포멧대상 문자열
     * @return 포멧된 문자열
     */
    public static String getOnlyNumFormat(String str) {
        return RegExUtils.removePattern(str, "[^0-9]");
    }

    /**
     * 날짜 8자리를 구분자(.)를 넣어서 반환.
     * <p>
     * <pre>
     * "2013.01.31" = formatDate("20130131", DEFAULT_DELIMITER_DATE);
     * "2013/01/31" = formatDate("20130131", "/");
     * </pre>
     * @param str (yyyymmdd)
     */
    public static String formatDate(String str, String delimiter) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (str.length() != 8) {
            return str;
        }
        if ((str.equals("00000000")) || (str.equals("       0"))) {
            return "";
        }
        return str.substring(0, 4) + delimiter + str.substring(4, 6) + delimiter + str.substring(6, 8);
    }

    /**
     * 시분초 6자리를 구분자(:)를 넣어서 반환.
     * <p>
     * <pre>
     * "16:45:55" = formatTime("164555", DEFAULT_DELIMITER_TIME);
     * "16-45-55" = formatTime("164555", ":");
     * </pre>
     * @param str (HHMMSS)
     * @return String (HH:MM:SS)
     */
    public static String formatTime(String str, String delimiter) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (str.length() != 6) {
            return str;
        }
        return str.substring(0, 2) + delimiter + str.substring(2, 4) + delimiter + str.substring(4, 6);
    }

    /**
     * 특수 문자 포함여부 체크.
     * <p>
     * <pre>
     * 지정된 Key가 없으면 Character제공 특수문자 체크하고 Key가 있으면 해당 키를 체크
     * </pre>
     * @param str   문자 조합
     */
    public static boolean isSpecialKey(String str) {
        return isSpecialKey(str, null);
    }

    /**
     * 특수 문자 포함여부 체크
     * <p>
     * <pre>
     * 지정된 Key가 없으면 Character제공 특수문자 체크하고 Key가 있으면 해당 키를 체크
     * </pre>
     * @param str   문자 조합
     * @param keys  문자 키 (default key = "!,*&^%$#@~;|>+")
     */
    public static boolean isSpecialKey(String str, String keys) {
        boolean bReturn = false;

        if (keys == null) {
            char cCharAt;
            for (int i = 0; i < str.length(); i++) {
                cCharAt = str.charAt(i);
                if (!Character.isJavaIdentifierPart(cCharAt)) {
                    bReturn = true;
                    break;
                }
            }
        } else {
            String newKeys = StringUtils.defaultIfBlank(keys, DEFAUlT_SPECIAL_KEY);
            str = StringUtils.defaultIfBlank(str, "");
            for (int i = 0; i < newKeys.length(); i++) {
                if (str.indexOf(newKeys.charAt(i)) > -1) {
                    bReturn = true;
                    break;
                }
            }
        }
        return bReturn;
    }

    /**
     * 카드번호 중간8자리 *표처리.
     * <p>
     *<pre>
     *	"1234-****-****-1234" = formatCardNoWithAsterisk("1234567856781234");
     *	"1234-****-****-1234" = formatCardNoWithAsterisk("1234-5678-5678-1234");
     *</pre>
     * @param cardNo  카드번호(16자리)
     * @return String  카드번호 (1111-****-****-1111)
     */
    public static String formatCardNoWithAsterisk(String cardNo) {

        String newCardNo = StringUtils.defaultIfBlank(cardNo, "");
        newCardNo = newCardNo.replaceAll("[-]", "");

        if (newCardNo.length() == 16) {
        	newCardNo = newCardNo.substring(0, 4) + "-****-****-" + newCardNo.substring(12, 16);
        }
        return newCardNo;
    }

    /**
     * 유효기간 *표처리.
     * <p>
     *<pre>
     *	"20**-*2" = formatCardValidDateWithAsterisk("201302");
     *	"20**-*2" = formatCardValidDateWithAsterisk("2013-02");
     *</pre>
     * @param validDate  유효기간(6자리)
     * @return String  유효기간 (11**-1*)
     */
    public static String formatCardValidDateWithAsterisk(String validDate) {

        String newValidDate = StringUtils.defaultIfBlank(validDate, "");
        newValidDate = newValidDate.replaceAll("[-]", "");

        if (newValidDate.length() == 6) {
            newValidDate = newValidDate.substring(0, 2) + "**-*" + newValidDate.substring(5, 6);
        }
        return newValidDate;
    }

    /**
     * 전화번호의 마지막 번호를 마스킹.
     * <p>
     * 뒷 4자리 마스킹 처리<br>
     * 입력값이 형식에 안 맞으면 "" 값을 리턴한다.
     * <pre>
     * ex) 010-1111-2222 -> 010-1111-****
     * </pre>
     */
    public static String formatPhoneNumberWithAsterisk(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber) || phoneNumber.length() <= 4) {
            return "";
        }
        int index = phoneNumber.length();
        return phoneNumber.substring(0, index - 4) + "****";
    }

    /**
     * email 주소를 마스킹.
     * <p>
     * 뒤 4자리 마스킹 처리<br>
     * 입력값이 형식에 안 맞으면 "" 값을 리턴한다.
     * <pre>
     * ex) aaabbcc@email.com -> aaab***@email.com
     * </pre>
     */
    public static String formatEMailWithAsterisk(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = email.indexOf('@');
        if (index < 1) {
            return "";
        }
        String[] emailArr = email.split("@");
        if (index > 4) {
        	return emailArr[0].substring(0, 4) + StringUtils.repeat("*", emailArr[0].length() - 4) + "@" + emailArr[1];
        } else {
            return StringUtils.repeat("*", index) + "@" + emailArr[1];
        }
    }

    /**
     * 인터넷 주소 항목을 마스킹.
     * <p>
     * ipV4 : 17~24 비트 영역<br>
     * ipV6 : 113~128 비트 영역<br>
     * 입력값이 형식에 안 맞으면 "" 값을 리턴한다.
     * <pre>
     * ex) 123.123.123.123 -> 123.123.***.123
     *     21DA:00D3:0000:2F3B:02AA:00FF:FE28:9C5A -> 21DA:00D3:0000:2F3B:02AA:00FF:FE28:****
     * </pre>
     */
    public static String formatIPWithAsterisk(String ip) {
        if (StringUtils.isBlank(ip)) {
            return "";
        }
        if (ip.indexOf('.') > -1) {// ip4
            String temp[] = ip.split("[.]");
            if (temp.length == 4) {
                temp[2] = "***";
                return StringUtils.join(temp, ".");
            } else {
                return "";
            }
        } else if (ip.indexOf(':') > -1) {// ip6
            String temp[] = ip.split("[:]");
            if (temp.length == 8) {
                temp[7] = "****";
                return StringUtils.join(temp, ":");
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * UTF-8 인코딩으로 byte 수에 맞춰 문자열 잘라내는 메소드
     * 한글이 바이트 수에 걸쳐서 깨지는 경우 해당 한글 문자를 생략한다.
     */
    public static String cutWordUtf8(String word, int byteLength) {
    	String newWord = new String(word);
        try {
            double MAX_UTF8_CHAR_LENGTH = 4.0;
            if (newWord.length() > byteLength) {
                newWord = newWord.substring(0, byteLength);
            }
            if (newWord.length() > byteLength / MAX_UTF8_CHAR_LENGTH) {
                int residual;
                residual = newWord.getBytes("UTF-8").length - byteLength;
                if (residual > 0) {
                    int tempResidual = residual, start, end = newWord.length();
                    while (tempResidual > 0) {
                        start = end - ((int) Math.ceil((double) tempResidual / MAX_UTF8_CHAR_LENGTH));
                        tempResidual = tempResidual - newWord.substring(start, end).getBytes("UTF-8").length;
                        end = start;
                    }
                    newWord = newWord.substring(0, end);
                }
            }
            return newWord;
        } catch (UnsupportedEncodingException e) {
        	log.error(e.getMessage(), e);
        }
        return newWord;
    }

    /**
     * 계좌 번호를 포매팅하기 위한 함수
     * 길이와 숫자만 들어왔는지 여부만 체크한다.
     * 사용법
     * <p/>
     * <pre>
     *     FormatUtil.formatAcno("32861211234567", false ) => "328-61-21-1234567"
     *     FormatUtil.formatAcno("32861211234567", true ) =>  "328-61-**-*******"
     * </pre>
     * @param acno
     * @return
     */
	public static String formatAcno(@NonNull String acno, boolean isMask) {
		final int LENGTH_ACNO = 14;

		if (StringUtils.isBlank(acno))
			return "";

		String str = acno.replace("-", "").trim();
		if (LENGTH_ACNO != str.length() || !StringUtils.isNumeric(str)) {
			log.error("잘못된 계좌번호");
			return acno;
		}

		if (isMask) {
			// 첫 5자리만 표시: 나머진 * 표시
			str = str.substring(0, 5) + StringUtils.repeat('*', LENGTH_ACNO - 5);
		}

		return FormatUtil.formatStringDelimiter(str, "-", new int[] { 3, 2, 2, 7 });
	}

    /**
     * 타사에서도 사용하기 위한 계좌번호 포맷팅 함수
     * - 타사인 경우 포맷팅 없이 계좌 번호 그대로 출력
     * @param bnkCode
     * @param acno
     * @param isMask
     * @return
     */
	public static String formatAcno(@NonNull String bnkCode, @NonNull String acno, boolean isMask) {
		if (StringUtils.isBlank(acno))
			return "";
		String str = acno.replace("-", "").trim();
		if (!StringUtils.isNumeric(str)) {
			log.error("잘못된 계좌번호");
			return acno;
		}

		if (isMask) {
			// 첫 4자리만 표시: 나머진 * 표시
			str = str.substring(0, 4) + StringUtils.repeat('*', str.length() - 4);
		}

		return str;
	}

    /**
     * 사업자 등록번호 포맷팅을 위한 함수
     * 길이와 숫자만 들어왔는지 여부만 체크한다.
     * <p/>
     * <pre>
     *     FormatUtil.formatBisno("1234567890") => "123-45-67890"
     * </pre>
     * @param bisno
     * @return
     */
    public static String formatBisno( @NonNull String bisno  )
    {
        final int LENGTH_BISNO = 10;

        if ( StringUtils.isBlank( bisno )) return "";
        String str = bisno.replace("-", "").trim();
        if( LENGTH_BISNO != str.length() || !StringUtils.isNumeric( str ) ) {
            log.error("잘못된 사업자등록번호");
            return bisno;
        }
        return FormatUtil.formatStringDelimiter( str, "-", new int[]{3,2,5}) ;
    }

    /**
     * 주민번호 포맷팅 함수
     * 길이와 숫자만 들어왔는지 여부만 체크한다.
     * 예) FormatUtil.formatRnno("1234561234567", false ) => "123456-1234567"
     * 예) FormatUtil.formatRnno("1234561234567", true  ) => "123456-1******"
     * @param rnno 주민번호 ( 13 자리 )
     * @param isMask 마스크 여부
     * @return
     */
    public static String formatRnno( @NonNull String rnno , boolean isMask )
    {
        final int LENGTH_RNNO = 13;

        if ( StringUtils.isBlank( rnno )) return "";
        String str = rnno.replace("-", "").trim();
        if( LENGTH_RNNO != str.length() || !StringUtils.isNumeric( str ) ) {
            log.error("잘못된 주민번호");
            return rnno;
        }

        if(isMask)
        {
            str = str.substring(0,7) + StringUtils.repeat("*",6);
        }

        return FormatUtil.formatStringDelimiter( str, "-", new int[]{6,7}) ;
    }

    /**
     * 전화번호를 포맷팅한 문자열로 리턴한다.
     * 전화번호 검증은 숫자여부와 최소 길이여부만 체크한다.
     * <p/>
     * <pre>
     * FormatUtil.formatPhone("1231234", false);     => "123-1234"
     * FormatUtil.formatPhone("12341234", true);     => "1234-****"
     * FormatUtil.formatPhone("0212341234", false);  => "02-1234-1234"
     * FormatUtil.formatPhone("021231234", false);   => "02-123-1234"
     * FormatUtil.formatPhone("03112341234", true);  => "031-1234-****"
     * FormatUtil.formatPhone("0311231234", false);  => "031-123-1234"
     * </pre>
     * @param phoneno 전화번호
     * @param isMask 마스킹 여부
     * @return
     */
    public static String formatPhone(@NonNull String phoneno, boolean isMask )
    {
        String str = phoneno.replace("-", "").trim();

        if ( StringUtils.isBlank( str ) ) return "";
        if( 7 > str.length() || !StringUtils.isNumeric( str ) ) {
            log.error("잘못된 전화번호");
            return phoneno;
        }

        int len = str.length();
        String result = null;

        // 지역 번호 없는 경우
        if( len == 7 )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{3,4}) ;
        // 지역 번호 없고 앞자리가 4자리인 경우
        else if( len == 8 )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{4,4}) ;
        // 서울(02) 이고 전화번호 앞자리가 3자리인 경우
        else if( len == 9 && str.startsWith("02") )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{2,3,4}) ;
        // 서울 이고, 전화번호 앞자리가 4자리인 경우
        else if( len == 10 && str.startsWith("02") )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{2,4,4}) ;
        // 전화번호 앞자리가 3자리인 경우, 지역 번호 포함
        else if( len == 10 && !str.startsWith("02") )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{3,3,4}) ;
        // 전화번호 앞자리가 4자리인 경우, 지역 번호 포함
        else if( len == 11 && !str.startsWith("02") )
            result = FormatUtil.formatStringDelimiter( str, "-", new int[]{3,4,4}) ;
        else {
            log.error("잘못된 전화번호");
            return phoneno;
        }


        // 마스킹 여부 확인
        if( isMask )
        {
            Matcher matcher = Pattern.compile( FORMATTER_PHONE_NUMBER ).matcher(result);
            if( matcher.find() ) {
                String replaceTaget = matcher.group(2);
                String maskStr = StringUtils.repeat('*', replaceTaget.length());

                result = result.replaceFirst(replaceTaget, maskStr );
            }
        }

        return result;
    }

    /**
     * email 주소를 마스킹.
     * <p>
     * email ID 앞 3자리 제외 마스킹 처리<br>
     * 입력값이 형식에 안 맞으면 "" 값을 리턴한다.
     * <pre>
     * ex) aaabbcc@email.com -> aaa****@email.com
     * </pre>
     */
    public static String formatEmail(@NonNull String email, boolean mask )
    {
        if ( StringUtils.isBlank( email )) return "";

        // email 검증 안 넣음
        if( mask )
        {
            String[] tokens = email.split("@");
            String emailId = tokens[0];
            String emailAdder = tokens[1];

            if( emailId.length() < 4 )
            {
                emailId = StringUtils.repeat('*', emailId.length() );
            }
            else {
                emailId = emailId.substring(0,3) + StringUtils.repeat('*', emailId.length()-3);
            }
            return emailId + "@" + emailAdder;
        }

        return email;
    }

    /**
     * 패스워드 길이만큼 '*' 표시함
     * <p/>
     * <pre>
     *     ****** -> FormatUtil.formatPassword( "123456" )
     * </pre>
     *
     * @param password
     * @return
     */
    public static String formatPassword( @NonNull String password )
    {
        return StringUtils.repeat("*", password.length());
    }

    /**
     * 고객 이름 마스킹
     * 홍길동 -> 홍**동
     * 맨앞 글자와 맨 뒤 글자만 표시 중간은 **
     */
    public static String getMaskedUserName( String name )
    {
        if( StringUtils.isBlank(name) )
        {
            return StringUtils.EMPTY;
        }

        // (사) (유) (주) 제거
        String newName = name.replaceAll("\\((.*?)\\)", "");

        if( StringUtils.isBlank(newName) )
        {
            return StringUtils.EMPTY;
        }

        String first =  String.valueOf(newName.charAt(0));
        String last =  (newName.length() > 1)? String.valueOf(newName.charAt(newName.length()-1)) : StringUtils.EMPTY;
        String middleToken = StringUtils.repeat("*", Math.min(2, Math.max(1, newName.length()-2)) ); // 최소 1개 최대 2개만 별표 표시

        String maskedName = first + middleToken + last;
        return maskedName;
    }

    /**
     * 사용자 ID
     * -> UserID 첫 5자 표시 나머지는 *
     * @param userId
     * @return
     */
    public static String getMaskedUserId( String userId )
    {
        String maskedUserId;


        if( StringUtils.isBlank( userId ) )
        {
            maskedUserId = StringUtils.EMPTY;
        }
        else {
            String newUserId = userId.trim();
            if( newUserId.length() > 5)
                maskedUserId = newUserId.substring(0,5) + StringUtils.repeat("*", newUserId.length() - 5);
            else {
                maskedUserId = newUserId.charAt(0) + StringUtils.repeat("*", newUserId.length()-1);
            }
        }
        return maskedUserId;
    }


    /**
     * OTP 시리얼 번호 마스킹
     * 12345678 -> 1234****
     * @param serialNo
     * @return
     */
    public static String getMaskedOtpSerialNo( String serialNo )
    {
        String maskedSerialNo;

        if( StringUtils.isBlank( serialNo) )
        {
            maskedSerialNo = StringUtils.EMPTY;
        }
        else {
            String newSerialNo = serialNo.trim();
            if( newSerialNo.length() > 4 )
                maskedSerialNo = newSerialNo.substring(0,4) + StringUtils.repeat("*", newSerialNo.length() - 4 );
            else
                maskedSerialNo = newSerialNo.charAt(0) + StringUtils.repeat("*", newSerialNo.length()-1);
        }
        return maskedSerialNo;
    }
    
    /**
     * str값이 숫자로만 이루어진지 체크해준다. 음수양수 둘다체크해준다.
     * @param str 형태의 숫자 문자열
     */
    public static boolean isIntegerType(String str) {
    	String FORMAT_NUMBER_PATTERN = "[0-9-]+";
    	return str.matches(FORMAT_NUMBER_PATTERN);
    }
    
    /**
     * str값이 숫자로만 이루어진지 체크해준다. 음수양수 둘다체크해준다.
     * @param str 형태의 숫자 문자열
     */
    public static boolean isDecimalType(String str) {
    	String FORMAT_NUMBER_PATTERN = "[0-9-.]+";
    	return str.matches(FORMAT_NUMBER_PATTERN);
    }
    
	/**
	 *  Json BigDecimal 변환시 null , "" 체크위한 로직
	*/
    public static BigInteger getBigIntegerValue(String str) {
		BigInteger bigInteger = new BigInteger("0");
		if(null == str || str == "") {
			return bigInteger;
		}
		if(isDecimalType(str) == true) {
			bigInteger = new BigDecimal(str).toBigInteger();
		}
		return bigInteger;
	}
    
	/**
	 *  Json BigDecimal 변환시 null , "" 체크위한 로직
	*/
    public static BigDecimal getBigDecimalValue(String str) {
		BigDecimal bigDecimal = new BigDecimal(0);
		if(null == str || str == "") {
			return bigDecimal;
		}
		if(isDecimalType(str) == true) {
			bigDecimal = new BigDecimal(str);
		}
		return bigDecimal;
	}

	/**
	 * 숫자외에 다른 값들을 전부 공백으로 변환.
	 * @param str
	 * @return
	 */
	public static String formatingNumber(String str) {
		if(null == str || "" == str) {
			return "";
		}
		String FORMAT_NUMBER_PATTERN = "[^0-9]+";
    	return str.replaceAll(FORMAT_NUMBER_PATTERN, "");
    }
	
	/**
	 * 숫자외에 다른 값들을 전부 공백으로 변환.
	 * @param str
	 * @return
	 */
	public static String formatingDecimal(String str) {
		if(null == str || "" == str) {
			return "";
		}
		String FORMAT_NUMBER_PATTERN = "[^0-9.]+";
    	return str.replaceAll(FORMAT_NUMBER_PATTERN, "");
    }


	/**
	 * 농협은행+농협중앙회 계좌번호 체계에 따른 기관코드 반환
	 * @param acno 계좌번호
	 * @return 기관코드(011=농협은행, 012=농협중앙회)
	 */
	public static String getCheckNHBankCd(String bankCd, String acno) {
		// 011,012가 아닐경우 농협은행이 아니므로, 입력된 은행코드를 그대로 반환
		if (!StringUtils.equalsAny(bankCd, "011", "012")) {
			return bankCd;
		}

		if (StringUtils.isBlank(acno)) {
			return bankCd;
		}

		// 농협은행,농협중앙회 기관코드 분류(계좌번호 패턴에 따른 분류)
		String newBankCd = "011";
		int len = acno.length();
		if (len == 10) {
			if (StringUtils.equalsAny(acno.substring(9,10), "9")) {
				newBankCd = "012";
			}
		} else if (len == 13) {
			if (StringUtils.equalsAny(acno.substring(12,13), "3", "4", "5", "9")) {
				newBankCd = "012";
			}
		} else if (len == 14) {
			if (StringUtils.equalsAny(acno.substring(0,3), "790", "791")) {
				newBankCd = "011";
			} else if (StringUtils.equalsAny(acno.substring(0,3), "792")) {
				newBankCd = "012";
			} else if (StringUtils.equalsAny(acno.substring(6,8), "64", "65")) {
				newBankCd = "011";
			} else if (StringUtils.equalsAny(acno.substring(6,8), "66", "67")) {
				newBankCd = "012";
			} else {
				newBankCd = "012";
			}
		}
		
		return newBankCd;
	}

}
