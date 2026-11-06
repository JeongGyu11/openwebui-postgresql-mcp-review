package kr.fasol.obix.front.common.util;

import kr.fasol.obix.core.exception.UserException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 업무구분명 : 
 * 업무구분명 : 공통 > Util > Admin 사용자 비밀번호 검증
 * 프로그램명 : Admin 사용자 비밀번호 검증
 * 설명 :
 *
 * 클래스 : java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 9.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 9.         정영탁          최초생성
 */
@UtilityClass
public class PswdValidUtil {

    private static final int USERID_MIN_LENGTH = 6;
    private static final int USERID_MAX_LENGTH = 13;

    private static final int PWSD_COMBI_2_MIN_LENGTH = 10;
    private static final int PWSD_COMBI_3_MIN_LENGTH = 8;

    private static final String PATTERN_SPECIAL_CHAR = "[!@#$%^&*()+=-]";
    private static final String PATTERN_SAME_CHAR = "(.)\\1\\1";

    private static final String USERID_PATTERN_REGEX = "^[0-9a-zA-z]{6,13}$";
    private static final String[] USERID_COMBI_PATTERNS = {"[0-9]", "[a-zA-z]"};

    private static final String[] PSWD_COMBI_PATTERNS = {"[0-9]", "[a-z]", "[A-Z]", PATTERN_SPECIAL_CHAR};

    /**
     * 비밀 번호 정책에 따른 비밀 번호 검증
     * 영문대문자, 영문소문자, 숫자, 특수문자의 4종류 조합으로 생성
     * - 2종류 조합시 10자리 이상, 3종류 조합시 8자리 이상
     * @param pswd 검증 대상 비밀번호
     * @return 검증 여부( 성공:true, 실패: false)
     */
    private static boolean isValidByPasswordPolicy(@NonNull final String pswd) {
        int matchCnt = 0;
        for (String iter : PSWD_COMBI_PATTERNS) {
            Pattern pattern = Pattern.compile(iter);
            Matcher matcher = pattern.matcher(pswd);
            if (matcher.find()) {
                matchCnt++;
            }
        }

        if (matchCnt == 2 && PWSD_COMBI_2_MIN_LENGTH <= StringUtils.length(pswd)) {
            return true;
        } else if (matchCnt >= 3 && PWSD_COMBI_3_MIN_LENGTH <= StringUtils.length(pswd)) {
            return true;
        }
        return false;
    }

    /**
     * 특수문자 포함여부
     * @param pswd 검증 대상 비밀번호
     * @return 검증 여부( 성공:true, 실패: false) - 특수문자가 포함되어야 함
     */
    private static boolean isValidSpecialChar(@NonNull final String pswd) {
        Pattern pattern = Pattern.compile(PATTERN_SPECIAL_CHAR);
        Matcher matcher = pattern.matcher(pswd);
        return matcher.find();
    }

    /**
     * 동일한 문자 체크 (예: aaa, 777)
     * @param pswd 검증 대상 비밀번호
     * @return 검증 여부( 성공:true, 실패: false ) - 동일한 문자/숫자가 있는 경우 false
     */
    public static boolean isValidDuplicate3Character(@NonNull final String pswd) {
        Pattern p = Pattern.compile(PATTERN_SAME_CHAR);
        Matcher m = p.matcher(pswd);
        if (!m.find()) return true;
        return false;
    }

    /**
     * 연속된 문자 체크 (예: abc, 123)
     * @param pswd
     * @return 검증 여부( 성공:true, 실패: false) - 연속된 글자가 있는 경우 false
     */
    private static boolean isValidContinuous3Charater(@NonNull final String pswd) {
        byte[] bytes = pswd.getBytes();

        for (int i = 1; i < bytes.length - 1; i++) {
            if (bytes[i] == (byte) ((bytes[i - 1] + bytes[i + 1]) / 2)
                    && 2 == Math.abs(bytes[i - 1] - bytes[i + 1])) {
                return false;
            }
        }
        return true;
    }

    private static String QWERTY_SEQUENCE = "qwertyuiop|asdfghjkl|zxcvbnm";

    /**
     * Qwerty 기반 배열 연속성 여부
     * - 4자 이상만 체크 한다.
     * - 가로만 해야 할 듯 하다. 세로까지 고려하기에 경우의 수가 많다.
     * @param pswd
     * @return
     */
    private static boolean isValidQwerySequence4(@NonNull final String pswd) {
        // forward, backward 둘다 되도록 처리함
        for (int i = 0; i < pswd.length() - 3; i++) {
            String str = pswd.substring(i, i + 4);

            if (!Pattern.matches("[a-z]{4}", str)) continue;

            if (QWERTY_SEQUENCE.contains(str)
                    || QWERTY_SEQUENCE.contains(StringUtils.reverse(str)))
                return false;
        }

        return true;
    }

    /**
     * 비밀번호만 가지고 검증하는 로직
     * @param pswd 검증 대상 비밀번호
     * @return 비밀번호 검증 여부 (true: 통과)
     */
    private static boolean isValidPassword(@NonNull final String pswd) {
        return (isValidSpecialChar(pswd)           // 비밀번호 검증 : 특수문자여부
                && isValidByPasswordPolicy(pswd)    // 비밀번호 검증 : 비밀번호 정책
                && isValidContinuous3Charater(pswd) // 비밀번호 검증 : 연속된 숫자/문자 3개 이상 여부
                && isValidDuplicate3Character(pswd) // 비밀번호 검증 : 동일 숫자/문자 3개 연속으로 있는경우
                && isValidQwerySequence4(pswd)
        );
    }

    /**
     * 비밀 번호와 유사성 유사성 여부
     * - 동일한 문자/숫자가 아이디/패스워드에 동일하게 들어가는지 확인
     * @param pswd 검증 대상 비밀번호
     * @param arg 비교대상 문자
     * @return 검증 여부( 성공:true, 실패: false) - 유사한 글자가 있는 경우 false
     */
    private static boolean isValidEqualCharacter(@NonNull final String pswd, @NonNull String arg) {
    	if (pswd.contains(arg)) { return false; }
    	return true;
    }

    /**
     * 비밀 번호와 유사성 유사성 여부
     * - 동일한 3글자 이상의 문자/숫자가 아이디/패스워드에 동일하게 들어가는지 확인
     * @param pswd 검증 대상 비밀번호
     * @param arg 비교대상 문자 ( 3글자 이상 )
     * @return 검증 여부( 성공:true, 실패: false) - 유사한 글자가 있는 경우 false
     */
    private static boolean isValidEqual3Character(@NonNull final String pswd, @NonNull String arg) {
    	int len = arg.length();
    	for (int i = 0; i < len - 2; i++) {
    		if (pswd.contains(arg.substring(i, i + 3))) return false;
    	}
    	return true;
    }

    /**
     * 아이디와 유사성 여부 체크 복수형
     * 복수의 문자열을 비밀번호와 넣어서 비교하기 위한 함수
     * @param pswd 검증 대상 비밀번호
     * @param args 복수의 문자열 Collection 형태로 전달
     * @return 검증 여부( 성공:true, 실패: false)  - 유사한 글자가 있는 경우 false
     */
    private static boolean isValidEqual3Character(@NonNull final String pswd, @NonNull List<String> args) {
        for (String arg : args) {
            if (!isValidEqual3Character(pswd, arg))
                return false;
        }
        return true;
    }

    /**
     * 비밀번호 유효일자 체크
     * - 비밀번호는 최근 변경일자 기준 60일 동안만 유효하다.
     * - 비밀번호 마지막 수정 일자를 기준으로 60일을 더한 일자와 오늘 일자를 변경한다.
     * @param lastChngDt 마지막 비밀번호 수정 일자
     * @return (lastChngDt+60) >= now : true // (lastChngDt+60) < now : false
     */
    private static int PSWD_VALID_DAYS = 60;

    @NonNull
    public static boolean isValidPswdChngDt(String lastChngDt) {
        if (StringUtils.isBlank(lastChngDt)) return true;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate validPswdDt = LocalDate.parse(lastChngDt, formatter).plusDays(60);

        // 비밀번호 유효일자가 오늘보다 이후이면 true, 아니면 false;
        return validPswdDt.isAfter(LocalDate.now());
    }

    /**
     * 사용자 ID 유효성 체크
     * - 영문(대소문자 구분없음)숫자 혼용 6~13
     * - 영문 숫자 조합 필수
     * @param bnknUserId
     * @return
     */
    private static boolean isValidUserId(String bnknUserId) {
        // 조합 여부
        int matchCnt = 0;
        for (String iter : USERID_COMBI_PATTERNS) {
            Pattern pattern = Pattern.compile(iter);
            Matcher matcher = pattern.matcher(bnknUserId);
            if (matcher.find()) {
                matchCnt++;
            }
        }

        if (2 > matchCnt) return false;

        return Pattern.matches(USERID_PATTERN_REGEX, bnknUserId);
    }

    /**
     * 비밀번호 테스트
     * @param pswd 비밀번호
     * @param userId 사용자 아이디
     * @param cpno 사용자 전화번호
     * @throws UserException
     */
    public static boolean checkValidPswd(@NonNull String pswd, String userId, String cpno) throws UserException {
        //비밀번호 유효성 체크
        // TODO 비밀번호유효성검증 오류코드 정의 필요

        if (StringUtils.isBlank(pswd)) {
            throw new UserException("BB3007").setDebuggingMessage("비밀번호 미입력");
        }
        if (!isValidByPasswordPolicy(pswd)) {
            throw new UserException("BB3009").setDebuggingMessage("영대문자, 영소문자, 숫자, 특수문자를 조합해서 8자리 이상.");
        }
        if (!isValidSpecialChar(pswd)) {
            throw new UserException("BB3009").setDebuggingMessage("특수문자 미포함");
        }
        if (!isValidContinuous3Charater(pswd)) {
            throw new UserException("BB3010").setDebuggingMessage("연속된 문자(숫자)");
        }
        if (!isValidDuplicate3Character(pswd)) {
            throw new UserException("BB3011").setDebuggingMessage("동일한 문자(숫자)");
        }
        if (StringUtils.isNotBlank(userId)) {
            if (!isValidEqualCharacter(pswd, userId)) {
                throw new UserException("BB3018").setDebuggingMessage("아이디와 동일한 문자 포함");
            }
        }
        if (StringUtils.isNotBlank(cpno)) {
            String cpno1 = cpno.startsWith("010") ? cpno.substring(3) : cpno;
            if (!isValidEqual3Character(pswd, cpno1)) {
                throw new UserException("BB3013").setDebuggingMessage("사용자의 휴대전화와 동일한 문자(숫자) 3개이상 포함");
            }
        }
        if (!isValidQwerySequence4(pswd)) {
            throw new UserException("BB3014").setDebuggingMessage("키보드상의 연속된 배열");
        }
        return true;
    }

    /**
     * 비밀번호 유효성 체크 패스워드 Only
     *
     * @param pswd 신규비밀번호
     * @return
     * @throws Exception
     */
    public static boolean checkValidPswd(@NonNull final String pswd) throws Exception {
        return checkValidPswd(pswd, null, null);
    }

}
