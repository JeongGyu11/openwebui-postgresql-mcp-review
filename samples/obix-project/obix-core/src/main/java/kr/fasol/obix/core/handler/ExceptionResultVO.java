package kr.fasol.obix.core.handler;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Exception > Exception 처리를 위한 VO
 * 프로그램명 : Exception 처리를 위한 VO
 * 설명 : 
 *
 * @Class  ExceptionResultVO.class
 * @author  정영탁
 * @since   2017-08-01
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-01        정영탁    최초생성
 */
@Data
public class ExceptionResultVO implements Serializable {
	@Serial
	private static final long serialVersionUID = -8923846262610587112L;

	private String errorCode; // 에러코드
	private String convertedErrorCode; // 변경된 에러코드
	private String errorMessage; // 에러메시지내용
	private String convertedErrorMessage; // 변경된 에러메시지내용
	private boolean isErrorCodeChanged = false; // 에러코드 변경여부
	private String errorType; // 에러유형(COMMON, WEB, BIZ, USER, AUTH, SERVICE, CRITICAL)
	private String orgErrorCode; // 원본 에러코드
	private String orgErrorMessage; // 원본 에러메시지내용
	private String orgErrorType; // 원본 에러유형
	private String programName; // 에러발생 프로그램명
	private String programLine; // 에러발생 프로그램 소스라인
	private String serverInfo; // 에러발생 서버정보
	private String returnServiceId; // 에러팝업 후 되돌아갈 서비스ID
	private Throwable exception; // Exception
	private String baseBizClass; // 에러발생 Base Package
	private String orgClassName; // 원본 에러발생 Class명
	private int orgLineNumber; // 원본 에러발생 프로그램 소스라인
	private String orgMethodName; // 원본 에러발생 프로그램 메소드명
	private String bizClassName; // 에러발생 Class명
	private int bizLineNumber; // 에러발생 프로그램 소스라인
	private String bizMethodName; // 에러발생 프로그램 메소드명
	private String backendChannelGuid = ""; // 에러발생 BT 채널 GUID
	private String backendServiceId; // 에러발생 BT 서비스ID
	private Map<String, Object> exceptionCommon; // Exception 기타정보
	private Map<String, Object> exceptionCause; // Exception 기타정보
	private Map<String, Object> additionalInfoMap = new HashMap<String, Object>(); // Exception 기타정보

	private String errorViewName; // 에러페이지 View명
	private String errorGuid; // 에러발생 GUID

}
