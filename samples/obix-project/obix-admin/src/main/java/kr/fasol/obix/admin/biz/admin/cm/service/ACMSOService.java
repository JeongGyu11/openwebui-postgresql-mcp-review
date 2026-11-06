package kr.fasol.obix.admin.biz.admin.cm.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import kr.fasol.obix.admin.biz.admin.cm.dto.*;
import kr.fasol.obix.admin.common.session.AdminSessionCommon;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.common.util.PswdValidUtil;
import kr.fasol.obix.admin.dao.admin.login.AdminLoginDAO;
import kr.fasol.obix.admin.dao.admin.sy.AdmTranTxnDAO;
import kr.fasol.obix.admin.dao.admin.sy.AdmUserBasDAO;
import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import kr.fasol.obix.admin.dto.biz.cm.AdmUserBasSesDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupBasQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmTranTxnQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserBasQryDTO;
import kr.fasol.obix.common.dto.entity.*;
import kr.fasol.obix.common.enums.CodeConstant;
import kr.fasol.obix.common.enums.SessionEnum;
import kr.fasol.obix.core.util.MailSenderUtil;
import kr.fasol.obix.common.util.SessionUtil;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 로그인
 * 프로그램명 : Admin 로그인 및 비밀번호 관리
 * 설명 : 
 *
 * 클래스 : ACMSOService.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 03. 17.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 03. 17.        정영탁			  최초작성
 */
@Slf4j
@Service
public class ACMSOService {

	@Resource(name = "AdminLoginDAO")
	private AdminLoginDAO adminLoginDAO;

	@Resource(name = "AdmUserBasDAO")
	private AdmUserBasDAO admUserBasDAO;

	@Resource(name = "AdmTranTxnDAO")
	private AdmTranTxnDAO admTranTxnDAO;

	@Autowired
	private MailSenderUtil mailSenderUtil;
	@Autowired
	private TemplateEngine templateEngine;

	private final static String GROUP_SYSADMIN = "SYSADMIN";

	/**
	 * 로그인 처리
	 */
	public ACMSO10001DTO ACMSO10001(ACMSO10001DTO input) throws Exception {
		boolean isSuccess = true;
		
		// 사용자 조회
		AdmUserBasSesDTO admUserBasSesDTO = adminLoginDAO.selectAdmUserBasByAuth( DTOUtil.convertDTO(input, AdmUserBasSesDTO.class) );
		if (admUserBasSesDTO == null) { //사용자아이디 미존재
			isSuccess = false;
			throw new UserException("ADM018").setDebuggingMessage("사용자 인증에 실패하였습니다. 사용자아이디와 비밀번호를 확인하신 후 다시 로그인 바랍니다");
		}
		log.debug("### 사용자 조회 결과 = {}", admUserBasSesDTO);
		String userStusCd = admUserBasSesDTO.getAdmUserStusCd();

		AdmUserBasDTO admUserBasDTO = AdmUserBasDTO.newBuilder()
				.userId(admUserBasSesDTO.getUserId())
				.admUserStusCd(userStusCd)
				.build();

		//어드민사용자상태코드 정상(10)인 경우
		if (CodeConstant.ADM_USER_STUS_CD.정상.getCode().equals(userStusCd)) {

//			if (SecurityUtil.SHA256(StringUtils.trimToEmpty(input.getUserId())).equals((AdmUserBasSesDTO.getUserPwd()))) { 
//				isSuccess = false;
//				AdmUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호초기화.getCode());
//				AdmUserBasSesDTO.setAdmLginRsltMsg("비밀번호 초기화 상태입니다.");
//			} else {
				int pwdErrCnt = (admUserBasSesDTO.getPwdErrNotms() != null ? admUserBasSesDTO.getPwdErrNotms().intValue() : 0);

				//pw check
				String encPwd = ObixSecurityUtil.SHA256(StringUtils.trimToEmpty(input.getUserPwd()));
				log.debug("### 입력된 비밀번호 암호화 = {}", encPwd);
				if (isSuccess && !encPwd.equals(admUserBasSesDTO.getUserPwd())) {
	
					int result = adminLoginDAO.updateAdmUserBasPwdErr(admUserBasDTO);
					if (result < 1) {
						log.error("비밀번호 오류횟수 업데이트 실패 !!!");
					}
					if (pwdErrCnt < 5) {
						admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호오류.getCode());
						admUserBasSesDTO.setAdmLginRsltMsg(AppMessageUtil.getMessage("ADM018")); //사용자 인증에 실패하였습니다. 사용자아이디와 비밀번호를 확인하신 후 다시 로그인 바랍니다
					} else {
						admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호오류초과.getCode());
						admUserBasSesDTO.setAdmLginRsltMsg("비밀번호 오류 횟수 초과되어 잠금처리되었습니다.");
						
						//어드민사용자상태코드 - 잠김 처리, 어드민사용자상태변경내역 테이블 이력 저장
						admUserBasDTO.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.잠김.getCode());
						result = adminLoginDAO.updateAdmUserBasStusCd(admUserBasDTO); 
						if (result < 1) {
							log.error("어드민사용자상태코드 업데이트 실패 !!!");
						}
						//어드민사용자상태변경내역 저장 
						AdmUserStusChngTxnDTO txnParam = AdmUserStusChngTxnDTO.newBuilder().userId(admUserBasSesDTO.getUserId()).build();
						txnParam.setBefUserStusCd(userStusCd);
						txnParam.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.잠김.getCode());
						txnParam.setChngUserId("SYSTEM");
						if (adminLoginDAO.insertAdmUserStusChngTxn(txnParam) < 1) {
							log.error("어드민사용상태변경내역 저장 실패 !!!");
						}
					}
					
					isSuccess = false;
				}
		
				//비밀번호 만기 check
				String lastPwdChngDt = admUserBasSesDTO.getLastPwdChngDt(); 
				int pwdChngTerm = DateUtil.daysBetween(lastPwdChngDt);
				if (isSuccess && pwdChngTerm > 90) {
					isSuccess = false;
					admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호만기.getCode());
					admUserBasSesDTO.setAdmLginRsltMsg("비밀번호가 만기되었습니다.\n 비밀번호 변경 후 로그인 바랍니다.");
					
				}
				//ip check
				String userIp = WebUtil.getClientIp();
				if (isSuccess && StringUtils.isNotEmpty(admUserBasSesDTO.getDesigIp()) && !userIp.startsWith(admUserBasSesDTO.getDesigIp())) {
					if (ProfileUtil.isProduction()) {
						isSuccess = false;
						admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.접근불가IP.getCode());
						admUserBasSesDTO.setAdmLginRsltMsg("해당 IP 접근불가 입니다.");
					}
				}
//			}

		}
		else { //사용자 상태 정상이 아닌경우
			//사용자 정지 상태
			if (CodeConstant.ADM_USER_STUS_CD.정지.getCode().equals(userStusCd)) { 
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.사용자아이디정지.getCode());
				admUserBasSesDTO.setAdmLginRsltMsg("사용자 정지 상태입니다.");
			}
			
			//비밀번호 초기화 상태
			else if (CodeConstant.ADM_USER_STUS_CD.초기화.getCode().equals(userStusCd)) { 
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호초기화.getCode());
				admUserBasSesDTO.setAdmLginRsltMsg("비밀번호 초기화 상태입니다.");
			}
			
			//비밀번호 오류초과 상태
			else if (CodeConstant.ADM_USER_STUS_CD.잠김.getCode().equals(userStusCd)) { 
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.비밀번호오류초과.getCode());
				admUserBasSesDTO.setAdmLginRsltMsg("비밀번호 오류 횟수 초과 상태입니다.");
			}
			
			//휴직 상태
			else if (CodeConstant.ADM_USER_STUS_CD.휴직.getCode().equals(userStusCd)) { 
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.사용자아이디만기_휴직.getCode());
				admUserBasSesDTO.setAdmLginRsltMsg("사용자 휴직 상태입니다.");
			}
			
			//퇴사 상태
			else if (CodeConstant.ADM_USER_STUS_CD.퇴사.getCode().equals(userStusCd)) { 
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.사용자아이디만기_퇴사.getCode());
				admUserBasSesDTO.setAdmLginRsltMsg("사용자 퇴사 상태입니다.");
			}
			
			else {
				isSuccess = false;
				admUserBasSesDTO.setAdmLginRsltCd("UNKNOWN");
				admUserBasSesDTO.setAdmLginRsltMsg("UNKNOWN USER STATUS");
				
				log.error("Admin Login Status Code is not Exist ... PLZ Check Login Source !!!");
			}
		}

		//로그인처리결과 
		if (isSuccess) {
			admUserBasSesDTO.setAdmLginRsltCd(CodeConstant.ADM_LGIN_RSLT_CD.성공.getCode()); 	//로그인 통과. 성공
			admUserBasSesDTO.setAdmLginRsltMsg(CodeConstant.ADM_LGIN_RSLT_CD.성공.getCodeName());

			AdmGroupBasQryDTO group = adminLoginDAO.getAdmUserMainGroup(AdmUserBasDTO.newBuilder().userId(input.getUserId()).build());

			// 사용자별 권한 & 메뉴 처리
			List<AdmScrenTranBasQryDTO> admScrenMenuList;
			if (group != null && !ProfileUtil.isProduction() && GROUP_SYSADMIN.equals(group.getGroupId())) {
				// production(운영)이 아니면 SYSADMIN 그룹은 전체 권한 부여
				admScrenMenuList = adminLoginDAO.listSysAdmScrenTran(AdmUserBasDTO.newBuilder().userId(input.getUserId()).build());
			} else {
				// 나머지는 사용자별 권한(전체허용메뉴 + 그룹권한메뉴 + 사용자권한메뉴) 부여
				admScrenMenuList = adminLoginDAO.listUserAdmScrenTran(AdmUserBasDTO.newBuilder().userId(input.getUserId()).build());
			}
			log.debug("### 조회된 메뉴 목록 = {}", admScrenMenuList);

			// 화면 표시 메뉴
			List<AdmScrenTranBasQryDTO> userMenuList = admScrenMenuList.stream()
					.filter(dto -> "Y".equals(dto.getMenuRepstYn()))
					.collect(Collectors.toList());
			log.debug("### 화면 표시 메뉴 = {}", userMenuList);

			/*
			 * 메뉴명 다국어 변경
			 */
			for (AdmScrenTranBasQryDTO dto : userMenuList) {
				try {
					String screnTranNm = AppMessageUtil.getMessage("I18N.menu.".concat(StringUtils.replace(dto.getScrenTranNm(), " ", "")));
					dto.setScrenTranNm(screnTranNm);
				} catch (NoSuchMessageException e) {
					log.error("### 메뉴명 다국어 코드 없음 = " + dto.getScrenTranNm());
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			String userMenu = mapper.writeValueAsString(userMenuList);

			// 즐겨찾기 조회
			AdmUserFvrtDtlDTO resUserFvrt = adminLoginDAO.getAdmUserFvrtDtl(DTOUtil.convertDTO(admUserBasSesDTO, AdmUserFvrtDtlDTO.class));
			String userFvrtList = (resUserFvrt != null && resUserFvrt.getFvrtCtnt() != null ? resUserFvrt.getFvrtCtnt() : "");

			// 비밀번호 오류횟수 초기화
			if (admUserBasDTO.getPwdErrNotms() != null && !admUserBasDTO.getPwdErrNotms().toString().equals("0")) {
				adminLoginDAO.insertAdmUserBasHst(admUserBasDTO);
				int result = adminLoginDAO.updateAdmUserBasPwdInit(admUserBasDTO);
				if (result < 1) {
					log.error("비밀번호 오류횟수 초기화 실패 !!!");  //로그인 성공 후 비번 오류횟수 초기화
				}
			}

			//세션설정
			List<AdmScrenTranBasDTO> emptylist = Collections.emptyList();
			String userMenuHisList = mapper.writeValueAsString(emptylist);
			
			AdminSessionCommon adminSessionCommon = AdminSessionCommon.builder()
					.admUserBasDTO(AdminSessionCommon.AdmUserBasDTO.builder()
							.userId(admUserBasSesDTO.getUserId())
							.userNm(admUserBasSesDTO.getUserNm())
							.cpNo(admUserBasSesDTO.getCpNo())
							.email(admUserBasSesDTO.getEmail())
							.desigIp(WebUtil.getClientIp())
							.lginTmsp(DateUtil.getToday(DateUtil.FORMATTER_YYYY_MM_DD_HH_MM_SS))
							.build())
					.admScrenMenuList(admScrenMenuList)
					.usrMenuHisList(userMenuHisList)
					.userMenuList(userMenu)
					.userFvrtList(userFvrtList)
					.userId(admUserBasSesDTO.getUserId())
					.userNm(admUserBasSesDTO.getUserNm())
					.build();
			AdminSessionHelper.put(adminSessionCommon);

			// Response DTO에서 개인정보 삭제
			admUserBasSesDTO.setUserPwd(null);
			admUserBasSesDTO.setCpNo(null);
			admUserBasSesDTO.setEmail(null);

		} else {
			admUserBasSesDTO.setUserPwd(null);
			admUserBasSesDTO.setUserNm(null);
			admUserBasSesDTO.setDesigIp(null);
			admUserBasSesDTO.setAdmUserStusCd(null);
			admUserBasSesDTO.setBrofcCd(null);
			admUserBasSesDTO.setCpNo(null);
			admUserBasSesDTO.setEmail(null);
			admUserBasSesDTO.setLastPwdChngDt(null);
			admUserBasSesDTO.setPwdErrNotms(null);
			admUserBasSesDTO.setDescCtnt(null);
		}

		/*
		 * 로그인 이력 저장
		 */
		AdmLginTxnDTO reqAdmLginTxnDTO = AdmLginTxnDTO.newBuilder()
				.userId(input.getUserId())
				.admLginRsltCd(admUserBasSesDTO.getAdmLginRsltCd())
				.connIp(WebUtil.getClientIp())
				.build();
		if (adminLoginDAO.insertAdmLginTxn(reqAdmLginTxnDTO) < 1) {
			log.error("로그인 이력 저장 실패 !!!");
		}

		return DTOUtil.convertDTO(admUserBasSesDTO, ACMSO10001DTO.class);
	}

	/**
	 * 사용자 설정 인증
	 */
	public ACMSO10101DTO ACMSO10101(ACMSO10101DTO input) throws Exception {
		ACMSO10101DTO output = new ACMSO10101DTO();

		AdmUserBasQryDTO admUserBasQryDTO = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasQryDTO.class));

		// 등록이 안된 사용자일 경우 에러 처리
		if (admUserBasQryDTO == null) {
			throw new UserException("ADM001").setDebuggingMessage("등록되지 않은 사용자");
		}

		// 운영 환경에서만 체크 함
		if (ProfileUtil.acceptsProfiles(Constants.ENV_PRODUCTION, Constants.ENV_DR)) {
			if (!StringUtils.equals(input.getUserNm(), admUserBasQryDTO.getUserNm()) || !StringUtils.equals(input.getCpNo(), admUserBasQryDTO.getCpNo())) {
				throw new UserException("ADM001").setDebuggingMessage("값이 유효하지 않는 사용자");
			}
		}

		// 사용자 상태에 따른 에러 처리
		if (CodeConstant.ADM_USER_STUS_CD.정상.getCode().equals(admUserBasQryDTO.getAdmUserStusCd()) || CodeConstant.ADM_USER_STUS_CD.초기화.getCode().equals(admUserBasQryDTO.getAdmUserStusCd())) {
			//TODO 사용자인증을 위한 정보 발송
			userCertificationMailSend(admUserBasQryDTO);

			// 정상 or 초기화 상태
			output = DTOUtil.convertDTO(admUserBasQryDTO, ACMSO10101DTO.class);
			return output;
		}
		else if (CodeConstant.ADM_USER_STUS_CD.잠김.getCode().equals(admUserBasQryDTO.getAdmUserStusCd())) {
			// 잠김 상태
			throw new UserException("ADM002").setDebuggingMessage("잠김 상태의 사용자");
		}
		else {
			// 그 이외의 상태
			throw new UserException("ADM003").setDebuggingMessage("이용할 수 없는 상태의 사용자");
		}
	}

	/**
	 * 사용자 인증을 위한 메일 발송
	 */
	private void userCertificationMailSend(AdmUserBasDTO admUserBasDTO) throws Exception {
		// 6자리 인증번호를 Random하게 생성
		String approvalNum = getRandomString(6);

		log.info("### 사용자 인증을 위한 메일 발송 ###");
		log.info("### 사용자ID = {}", admUserBasDTO.getUserId());
		log.info("### OTP 번호 = {}", approvalNum);

		// 승인번호를 세션에 저장
		SessionUtil.put(SessionEnum.SELF_CERT_SESSION, approvalNum);

		// 메일 발송
		try {
			Map<String, Object> mailDataMap = new HashMap<>();
			mailDataMap.put("approvalNum", approvalNum);

			mailSenderUtil.sendMessage(MailSenderUtil.MailMessageDto.builder()
					.fromEmail(EnvironmentUtil.getProperty("MAIL.FROM.EMAIL", ""))
					.fromName(EnvironmentUtil.getProperty("MAIL.FROM.NAME", ""))
					.toEmail(admUserBasDTO.getEmail())
					.subject("[ObiX 관리 시스템] 비밀번호 재설정을 위한 승인번호")
					.text(getTemplateHtml("user_certification", mailDataMap))
					.text("인증번호 : " + approvalNum)
					.build());
		} catch (Exception e) {
			log.error("[이메일 발송][발송실패]", e);
		}
	}
	/**
     * Random한 승인번호 생성
     */
    private static String getRandomString(int size) {
		char[] digits = "0123456789".toCharArray();
		Random r = new Random(System.currentTimeMillis());

		StringBuilder buf = new StringBuilder();
		r.setSeed(System.currentTimeMillis());
		for (int i = 0; i < size; i++) {
			buf.append(digits[r.nextInt(10)]);
		}

		return buf.toString();
    }
	/**
	 * 메일 발송 템플릿 반환
	 */
	public String getTemplateHtml(String templateName, Map<String, Object> mailDataMap) {
		try {
			// 메일 본문 템플릿 설정
			Context context = new Context(Locale.KOREA);
			context.setVariables(mailDataMap);
			return templateEngine.process(templateName, context);
		} catch (Exception e) {
			log.error("[메일 템플릿 생성 실패]", e);
		}
		return null;
	}

	/**
	 * 인증번호 검증
	 */
	public ACMSO10201DTO ACMSO10201(ACMSO10201DTO input) throws Exception {
		ACMSO10201DTO output = new ACMSO10201DTO();

		// 세션에서 승인번호 확인
		String approvalNum = SessionUtil.getString(SessionEnum.SELF_CERT_SESSION);
		if (approvalNum == null) {
			throw new UserException("BB2048").setDebuggingMessage("OTP 에러");
		}

		//TODO OTP 검증 로직
		if (!StringUtils.equals(approvalNum, input.getOtpPwd())) {
			throw new UserException("BB2047").setDebuggingMessage("OTP가 일치하지 않음.(세션값 = " + approvalNum + ")(입력값 = " + input.getOtpPwd() + ")");
		}

		// 승인번호 세션 제거
		SessionUtil.remove(SessionEnum.SELF_CERT_SESSION);

		output = ACMSO10201DTO.builder().userId(input.getUserId()).build();
		return output;
	}

	/**
	 * 비밀번호 재설정 처리
	 */
	public boolean ACMSO10301(ACMSO10301DTO input) throws Exception {
		// 등록자/수정자 기본값 설정
		input.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		input.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		//비밀번호 유효성 체크
		PswdValidUtil.checkValidPswd(input.getUserPwd(), input.getUserId(), null);

		//입력받은 신규 비밀번호 암호화
		input.setUserPwd(ObixSecurityUtil.SHA256(input.getUserPwd()));

		input.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.초기화.getCode());

		//최근 5회 변경 비밀번호 중 동일 비밀번호 건수 가져오기
		if (adminLoginDAO.getAdmUserSamePwdCnt(DTOUtil.convertDTO(input, AdmUserBasDTO.class)) > 0) {
			// 최근에 사용한 비밀번호로 변경할 수 없습니다.
			throw new UserException("ADM004").setDebuggingMessage("최근 5회 비밀번호 이미 존재");
		}

		//비밀번호 재설정 이력 생성
		adminLoginDAO.insertAdmUserPwdChngTxn(DTOUtil.convertDTO(input, AdmUserPwdChngTxnDTO.class));

		//비밀번호 재설정
		input.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.정상.getCode());
		AdmUserBasDTO admUserBasDTO = DTOUtil.convertDTO(input, AdmUserBasDTO.class);
		adminLoginDAO.insertAdmUserBasHst(admUserBasDTO);
		adminLoginDAO.updateAdmUserBasPwd(admUserBasDTO);

		return true;
	}

	/**
	 * 비밀번호 변경 처리
	 */
	public boolean ACMSO10401(ACMSO10401DTO input) throws Exception {
		// 등록자/수정자 기본값 설정
		input.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		input.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		//비밀번호 유효성 체크
		PswdValidUtil.checkValidPswd(input.getUserPwd(), input.getUserId(), null);

		//입력받은 신규 비밀번호 암호화
		input.setUserPwd(ObixSecurityUtil.SHA256(input.getUserPwd()));

		//현재 비밀번호 검증
		AdmUserBasQryDTO userdto = admUserBasDAO.selectAllAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasQryDTO.class));
		if (!userdto.getUserPwd().equals(ObixSecurityUtil.SHA256(input.getNowUserPwd()))) {
			//현재 비밀번호가 틀렸습니다.
			throw new UserException("ADM005").setDebuggingMessage("현재 비밀번호 오류");
		}

		input.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.초기화.getCode());

		//최근 5회 변경 비밀번호 중 동일 비밀번호 건수 가져오기
		if (adminLoginDAO.getAdmUserSamePwdCnt(DTOUtil.convertDTO(input, AdmUserBasDTO.class)) > 0) {
			// 최근에 사용한 비밀번호로 변경할 수 없습니다.
			throw new UserException("ADM004").setDebuggingMessage("최근 비밀번호 이미 존재");
		}

		//비밀번호 재설정 이력 생성
		adminLoginDAO.insertAdmUserPwdChngTxn(DTOUtil.convertDTO(input, AdmUserPwdChngTxnDTO.class));

		//비밀번호 재설정
		input.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.정상.getCode());
		AdmUserBasDTO admUserBasDTO = DTOUtil.convertDTO(input, AdmUserBasDTO.class);
		adminLoginDAO.insertAdmUserBasHst(admUserBasDTO);
		adminLoginDAO.updateAdmUserBasPwd(admUserBasDTO);

		return true;
	}

	/**
	 * 어드민 사용자 거래내역 insert.
	 * - 사용자 접근 화면 남김
	 * - 사용자 메뉴의 view 화면만 남김
	 */
	public void insertAdmTranTxn(AdmTranTxnQryDTO req) throws Exception {
		int cnt = admTranTxnDAO.insertAdmTranTxn(req);
		if (cnt < 1) {
			log.error("어드민 사용자 거래내역[ADM_TRAN_TXN] insert fail !!!");
		}
	}

}
