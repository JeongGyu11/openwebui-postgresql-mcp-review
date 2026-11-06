package kr.fasol.obix.admin.biz.admin.cm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import kr.fasol.obix.core.util.CryptoJsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.common.enums.SessionEnum;
import kr.fasol.obix.common.util.SessionUtil;
import io.swagger.v3.oas.annotations.Operation;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMSO10001DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMSO10101DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMSO10201DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMSO10301DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMSO10401DTO;
import kr.fasol.obix.admin.biz.admin.cm.service.ACMSOService;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 로그인
 * 프로그램명 : Admin 로그인 및 비밀번호 관리
 * 설명 : 
 *
 * 클래스 : ACMSOController.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 03. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 03. 13.        정영탁			  최초작성
 */
@Controller
@RequestMapping(value = "/admin/cm/so")
@Tag(name = "공통 > 로그인")
public class ACMSOController extends AbstractBaseController {

	/** AdminLoginService */
	@Resource(name = "ACMSOService")
	private ACMSOService aCMSOService;


	@Operation(summary = "로그인 페이지")
	@RequestMapping(value = "/ACMSO10000.view")
	public String ACMSO10000(
			@RequestParam(name="returnUrl", required=false) String returnUrl,
			@RequestParam(name="returnParam", required=false) String returnParam,
			ModelMap map, HttpServletRequest request) throws Exception {

		/*
		 * Return URL 설정
		 * - 로그인 후 돌아올 URL을 세션에 저장
		 */
		if (StringUtils.isNotBlank(returnUrl) && returnUrl.contains("/admin/cm/so/ACMSO") == false) {
			SessionUtil.put(SessionEnum.NEXT_USER_URL, returnUrl);
		}
		if (StringUtils.isNotBlank(returnParam)) {
			SessionUtil.put(SessionEnum.NEXT_USER_URL_PARAM, returnParam);
		}

		// request attribute 설정
		map.put("currentLocaleCode", RequestContextUtils.getLocale(request));

		return getDefaultViewName(request);
	}

	@Operation(summary = "로그인 수행")
	@RequestMapping(value = "/ACMSO10001.json")
	public Response<?> loginProcess(@ModelAttribute ACMSO10001DTO input, HttpServletRequest request) throws Exception {

		/*
		 * 기존 로그인세션 정보가 남아있다면 제거한다.
		 */
		if (AdminSessionHelper.isLogin()) {
			AdminSessionHelper.destroy();
		}

		// 비밀번호 복호화(Crypto.JS)
		if (StringUtils.isNotBlank(input.getUserPwd())) {
			input.setUserPwd( CryptoJsUtil.decryptAES(input.getUserPwd(), input.getUserId()) );
		}

		/*
		 * 로그인 수행
		 */
		ACMSO10001DTO output = aCMSOService.ACMSO10001(input);

		/*
		 * 로그인 후 되돌아갈 페이지(returnUrl) 지정
		 * 1. 세션에 기록된 returnUrl이 있으면(메뉴를 클릭하여 이동한 경우), 해당 페이지로 이동
		 * 2. 로그인 버튼을 클릭하여 이도한 경우, 기본 페이지로 이동
		 */
		String returnUrl = SessionUtil.getString(SessionEnum.NEXT_USER_URL);
		String returnParam = SessionUtil.getString(SessionEnum.NEXT_USER_URL_PARAM);
		SessionUtil.remove(SessionEnum.NEXT_USER_URL);
		SessionUtil.remove(SessionEnum.NEXT_USER_URL_PARAM);
		if (returnUrl != null) {
			output.setReturnUrl(returnUrl);
			output.setReturnUrlParam(returnParam);
		}

		return Response.ok(output);
	}

	@Operation(summary = "로그아웃 수행")
	@RequestMapping(value = "/ACMSO10002.json")
	public Response<?> ACMSO10002(HttpServletRequest request) throws Exception {

		/*
		 * 로그아웃 수행
		 */
		if (AdminSessionHelper.isLogin()) {
			AdminSessionHelper.destroy();
		}

		return Response.ok(null);
	}

	@Operation(summary = "비밀번호 재설정")
	@RequestMapping(value = "/ACMSO10100.frag")
	public String ACMSO10100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "사용자 정보 인증")
	@RequestMapping(value = "/ACMSO10101.json")
	public Response<?> ACMSO10101(@ModelAttribute ACMSO10101DTO input, HttpServletRequest request) throws Exception {

		ACMSO10101DTO res = aCMSOService.ACMSO10101(input);

		return Response.ok(res);
	}

	@Operation(summary = "인증번호 입력")
	@RequestMapping(value = "/ACMSO10200.frag")
	public String ACMSO10200(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "인증번호 검증")
	@RequestMapping(value = "/ACMSO10201.json")
	public Response<?> ACMSO10201(@ModelAttribute ACMSO10201DTO input, HttpServletRequest request) throws Exception {

		ACMSO10201DTO res = aCMSOService.ACMSO10201(input);

		return Response.ok(res);
	}

	@Operation(summary = "비밀번호 입력")
	@RequestMapping(value = "/ACMSO10300.frag")
	public String ACMSO10300(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "비밀번호 재설정 처리")
	@RequestMapping(value = "/ACMSO10301.json")
	public Response<?> ACMSO10301(@ModelAttribute ACMSO10301DTO input, HttpServletRequest request) throws Exception {

		boolean res = aCMSOService.ACMSO10301(input);

		return Response.ok(res);
	}

	@Operation(summary = "비밀번호 변경")
	@RequestMapping(value = "/ACMSO10400.frag")
	public String ACMSO10400(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "비밀번호 재설정 처리")
	@RequestMapping(value = "/ACMSO10401.json")
	public Response<?> ACMSO10401(@ModelAttribute ACMSO10401DTO input, HttpServletRequest request) throws Exception {

		boolean res = aCMSOService.ACMSO10401(input);

		return Response.ok(res);
	}

}
