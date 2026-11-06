package kr.fasol.obix.admin.biz.admin.sy.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.enums.ExcelConstant;
import kr.fasol.obix.core.util.ControllerUtils;
import kr.fasol.obix.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.common.dto.entity.AdmScrenTranBasDTO;
import kr.fasol.obix.core.view.ExcelXlsxView;
import io.swagger.v3.oas.annotations.Operation;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10001DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10002DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10101DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10201DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10202DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10203DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU20001DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU20002DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU20101DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU20102DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU20201DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU30001DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU30002DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40001DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40002DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40101DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40102DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40103DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40104DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40201DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40202DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU50001DTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU50002DTO;
import kr.fasol.obix.admin.biz.admin.sy.service.ASYAUService;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 시스템관리 > 권한관리
 * 프로그램명 : 권한관리
 * 설명 : 
 *
 * 클래스 : ASYAUController
 * 작성자 : 최정환
 * 작성일 : 2023. 03. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 04. 20.      서예원		      관리자그룹관리 23.04.18 변경사항 수정
 * 2023. 04. 06.      서예원		      관리자 그룹 조회/삭제 기능 수정
 * 2023. 04. 03.      김상훈		      사용자관련 수정
 * 2023. 03. 11.      최정환			  최초작성
 * 2023. 03. 09.      고길오            관리자그룹 추가
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/sy/au")
@Tag(name = "시스템관리 > 권한관리")
public class ASYAUController extends AbstractBaseController {
	
	@Resource(name = "ASYAUService")
	private ASYAUService aSYAUService;

	@Operation(summary = "화면거래 관리")
	@RequestMapping(value = "/ASYAU10000.view")
	public String ASYAU10000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "화면거래 목록 조회")
	@RequestMapping(value = "/ASYAU10001.json")
	public Response<?> ASYAU10001(@ModelAttribute ASYAU10001DTO input, HttpServletRequest request) throws Exception{
		ASYAU10001DTO output = new ASYAU10001DTO();
		
		/*========================================================
		  - 화면거래 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10001(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "화면거래 목록 엑셀")
	@RequestMapping(value = "/ASYAU10002.excl")
	public Object ASYAU10002(@ModelAttribute ASYAU10002DTO input, HttpServletRequest request) throws Exception{
		ASYAU10002DTO output = new ASYAU10002DTO();
		
		/*========================================================
		  - 화면거래 목록 엑셀 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10002(input);

		/*
		 * 엑셀 다운로드를 위한 데이터 작성
		 */
		List<List<String>> excelDataList = new ArrayList<>();

		if (!output.getGrid().isEmpty()) {
			// header
			excelDataList.add(Arrays.asList("No.", "업무대분류", "업무중분류", "화면ID", "화면명", "화면구분", "URL주소", "메뉴표시여부", "전체허용여부", "민감정보포함", "표시순서", "사용여부"));
			// data
			for (int i = 0; i < output.getGrid().size(); i++) {
				AdmScrenTranBasDTO item = output.getGrid().get(i);
				excelDataList.add(Arrays.asList(
						String.valueOf(i + 1),
						item.getAdmBzwkLclsCd(),
						item.getAdmBzwkMclsCd(),
						item.getScrenTranId(),
						item.getScrenTranNm(),
						item.getScrenTranDvCd(),
						item.getUrlAddr(),
						item.getMenuRepstYn(),
						item.getWholAllowYn(),
						item.getSstvInfoYn(),
						item.getRepstOrdr().toString(),
						item.getUseYn()
						));
			}
		}

		/*
		 * 엑셀 생성 후 다운로드
		 */
		Map<String, Object> result = new HashMap<>();
		result.put(ExcelConstant.EXCEL_DOWNLOAD_TYPE, ExcelConstant.EXCEL_DOWNLOAD_CUSTOM);
		result.put(ExcelConstant.EXCEL_DATA, excelDataList);
		result.put(ExcelConstant.EXCEL_FILE_NAME, "화면거래목록");

		ModelAndView mv = new ModelAndView();
		mv.addAllObjects(result);
		mv.setView(new ExcelXlsxView());

		return mv;
	}

	@Operation(summary = "화면거래 미등록 URL 현황")
	@RequestMapping(value = "/ASYAU10100.frag")
	public String ASYAU10100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "화면거래 미등록 URL 현황 조회")
	@RequestMapping(value = "/ASYAU10101.json")
	public Response<?> ASYAU10101(@ModelAttribute ASYAU10101DTO input, HttpServletRequest request) throws Exception{
		ASYAU10101DTO output = new ASYAU10101DTO();
		
		/*========================================================
		  - 화면거래 미등록 URL 현황 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10101(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "화면거래 설정")
	@RequestMapping(value = "/ASYAU10200.frag")
	public String ASYAU10200(HttpServletRequest request, ModelMap model) throws Exception {
		// request attribute 설정
		model.addAttribute("screnTranId", ControllerUtils.getStringParameter(request, "screnTranId", ""));
		
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "화면거래 하위메뉴 목록")
	@RequestMapping(value = "/ASYAU10201.json")
	public Response<?> ASYAU10201(@ModelAttribute ASYAU10201DTO input, HttpServletRequest request) throws Exception{
		ASYAU10201DTO output = new ASYAU10201DTO();
		
		/*========================================================
		  - 화면거래 하위메뉴 목록 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10201(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "화면거래 설정정보 조회")
	@RequestMapping(value = "/ASYAU10202.json")
	public Response<?> ASYAU10202(@ModelAttribute ASYAU10202DTO input, HttpServletRequest request) throws Exception{
		ASYAU10202DTO output = new ASYAU10202DTO();
		
		/*========================================================
		  - 화면거래 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10202(input);
		
		return Response.ok(output);
	}
	

	@Operation(summary = "화면거래 설정정보 저장")
	@RequestMapping(value = "/ASYAU10203.json")
	public Response<?> ASYAU10203(@ModelAttribute ASYAU10203DTO input, HttpServletRequest request) throws Exception{
		ASYAU10203DTO output = new ASYAU10203DTO();
		
		/*========================================================
		  - 화면거래 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU10203(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 관리")
	@RequestMapping(value = "/ASYAU20000.view")
	public String ASYAU20000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "그룹ROLE 목록 조회")
	@RequestMapping(value = "/ASYAU20001.json")
	public Response<?> ASYAU20001(@ModelAttribute ASYAU20001DTO input,
			@RequestParam(value = "page", required = false) Integer page,
		    @RequestParam(value = "rows", required = false) Integer rows,
		    HttpServletRequest request) throws Exception{
		ASYAU20001DTO output = new ASYAU20001DTO();
		
		/*========================================================
		  - 그룹ROLE 목록 조회 서비스 호출
		  ========================================================*/
		if (page == null) {
			page = 1;
		}
		
		if (rows == null) {
			rows = 10;
		}
		
	    input.setPageNo(page);
	    input.setPageSize(rows);
	    
		output = aSYAUService.ASYAU20001(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 목록 엑셀")
	@RequestMapping(value = "/ASYAU20002.json")
	public Response<?> ASYAU20002(@ModelAttribute ASYAU20002DTO input, HttpServletRequest request) throws Exception{
		ASYAU20002DTO output = new ASYAU20002DTO();
		
		/*========================================================
		  - 그룹ROLE 목록 엑셀 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU20002(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 설정")
	@RequestMapping(value = "/ASYAU20100.frag")
	public String ASYAU20100(HttpServletRequest request, ModelMap model) throws Exception {
		// request attribute 설정
		model.addAttribute("groupId", ControllerUtils.getStringParameter(request, "groupId", ""));

		return getDefaultViewName(request);
	}
	
	@Operation(summary = "그룹ROLE 설정 조회")
	@RequestMapping(value = "/ASYAU20101.json")
	public Response<?> ASYAU20101(@ModelAttribute ASYAU20101DTO input, HttpServletRequest request) throws Exception{
		ASYAU20101DTO output = new ASYAU20101DTO();
		
		/*========================================================
		  - 그룹ROLE 설정 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU20101(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 설정 저장")
	@RequestMapping(value = "/ASYAU20102.json")
	public Response<?> ASYAU20102(@RequestBody ASYAU20102DTO input, HttpServletRequest request) throws Exception{
		ASYAU20102DTO output = new ASYAU20102DTO();
		
		/*========================================================
		  - 그룹ROLE 설정 저장 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU20102(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 사용자 목록")
	@RequestMapping(value = "/ASYAU20200.frag")
	public String ASYAU20200(HttpServletRequest request, ModelMap model) throws Exception {
		// request attribute 설정
		model.addAttribute("groupId", ControllerUtils.getStringParameter(request, "groupId", ""));
		
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "그룹ROLE 사용자 목록 조회")
	@RequestMapping(value = "/ASYAU20201.json")
	public Response<?> ASYAU20201(@ModelAttribute ASYAU20201DTO input, HttpServletRequest request) throws Exception{
		ASYAU20201DTO output = new ASYAU20201DTO();
		
		/*========================================================
		  - 그룹ROLE 사용자 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU20201(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 권한 관리")
	@RequestMapping(value = "/ASYAU30000.view")
	public String ASYAU30000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "그룹ROLE 권한 목록 조회")
	@RequestMapping(value = "/ASYAU30001.json")
	public Response<?> ASYAU30001(@ModelAttribute ASYAU30001DTO input,
			@RequestParam(value = "page", required = false) Integer page,
		    @RequestParam(value = "rows", required = false) Integer rows,
		    HttpServletRequest request) throws Exception{
		ASYAU30001DTO output = new ASYAU30001DTO();
		
		if (page == null) {
			page = 1;
		}
		
		if (rows == null) {
			rows = 10;
		}
		
	    input.setPageNo(page);
	    input.setPageSize(rows);
	    
		/*========================================================
		  - 그룹ROLE 권한 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU30001(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "그룹ROLE 권한 목록 엑셀")
	@RequestMapping(value = "/ASYAU30002.json")
	public Response<?> ASYAU30002(@ModelAttribute ASYAU30002DTO input, HttpServletRequest request) throws Exception{
		ASYAU30002DTO output = new ASYAU30002DTO();
		
		/*========================================================
		  - 그룹ROLE 권한 목록 엑셀 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU30002(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 관리")
	@RequestMapping(value = "/ASYAU40000.view")
	public String ASYAU40000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "사용자 목록 조회")
	@RequestMapping(value = "/ASYAU40001.json")
	public Response<?> ASYAU40001(@ModelAttribute ASYAU40001DTO input,
			@RequestParam(value = "page", required = false) Integer page,
		    @RequestParam(value = "rows", required = false) Integer rows,
		    HttpServletRequest request) throws Exception{
		ASYAU40001DTO output = new ASYAU40001DTO();
		
		if (page == null) {
			page = 1;
		}
		
		if (rows == null) {
			rows = 10;
		}
		
	    input.setPageNo(page);
	    input.setPageSize(rows);
		/*========================================================
		  - 사용자 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40001(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 목록 엑셀")
	@RequestMapping(value = "/ASYAU40002.excl")
	public Object ASYAU40002(@ModelAttribute ASYAU40002DTO input, HttpServletRequest request) throws Exception{
		ASYAU40002DTO output = new ASYAU40002DTO();
		
		/*========================================================
		  - 사용자 목록 엑셀 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40002(input);

		/*
		 * 엑셀 다운로드를 위한 데이터 작성
		 */
		List<List<String>> excelDataList = new ArrayList<>();

		if (!output.getGrid().isEmpty()) {
			// header
			excelDataList.add(Arrays.asList("No.", "아이디", "이름", "사용자상태", "전화번호", "이메일", "그룹", "최종로그인 일시"));
			// data
			for (int i = 0; i < output.getGrid().size(); i++) {
				ASYAU40002DTO.Grid item = output.getGrid().get(i);
				excelDataList.add(Arrays.asList(
						String.valueOf(i + 1),
						item.getUserId(),
						item.getUserNm(),
						item.getAdmUserStusCd(),
						item.getCpNo(),
						item.getEmail(),
						item.getGroupNm(),
						DateUtil.getString(item.getLginTmsp(), DateUtil.FORMATTER_YYYY_MM_DD_HH_MM_SS)
				));
			}
		}

		/*
		 * 엑셀 생성 후 다운로드
		 */
		Map<String, Object> result = new HashMap<>();
		result.put(ExcelConstant.EXCEL_DOWNLOAD_TYPE, ExcelConstant.EXCEL_DOWNLOAD_CUSTOM);
		result.put(ExcelConstant.EXCEL_DATA, excelDataList);
		result.put(ExcelConstant.EXCEL_FILE_NAME, "사용자목록");

		ModelAndView mv = new ModelAndView();
		mv.addAllObjects(result);
		mv.setView(new ExcelXlsxView());

		return mv;
	}
	
	@Operation(summary = "사용자 설정")
	@RequestMapping(value = "/ASYAU40100.frag")
	public String ASYAU40100(HttpServletRequest request, ModelMap model) throws Exception {
		// request attribute 설정
		model.addAttribute("userId", ControllerUtils.getStringParameter(request, "userId", ""));

		return getDefaultViewName(request);
	}
	
	@Operation(summary = "사용자 설정정보 조회")
	@RequestMapping(value = "/ASYAU40101.json")
	public Response<?> ASYAU40101(@ModelAttribute ASYAU40101DTO input, HttpServletRequest request) throws Exception{
		ASYAU40101DTO output = new ASYAU40101DTO();
		
		/*========================================================
		  - 사용자 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40101(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 설정정보 저장")
	@RequestMapping(value = "/ASYAU40102.json")
	public Response<?> ASYAU40102(@ModelAttribute ASYAU40102DTO input, HttpServletRequest request) throws Exception{
		ASYAU40102DTO output = new ASYAU40102DTO();

		input.setAllUserId(AdminSessionHelper.getAdminUserId());
		
		/*========================================================
		  - 사용자 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40102(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 비밀번호 초기화")
	@RequestMapping(value = "/ASYAU40103.json")
	public Response<?> ASYAU40103(@ModelAttribute ASYAU40103DTO input, HttpServletRequest request) throws Exception{
		ASYAU40103DTO output = new ASYAU40103DTO();
		
		/*========================================================
		  - 사용자 비밀번호 초기화 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40103(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 그룹ROLE 목록 조회")
	@RequestMapping(value = "/ASYAU40104.json")
	public Response<?> ASYAU40104(@ModelAttribute ASYAU40104DTO input, HttpServletRequest request) throws Exception{
		ASYAU40104DTO output = new ASYAU40104DTO();
		
		/*========================================================
		  - 사용자 그룹ROLE 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40104(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 권한 설정")
	@RequestMapping(value = "/ASYAU40200.frag")
	public String ASYAU40200(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "사용자 권한 정보 조회")
	@RequestMapping(value = "/ASYAU40201.json")
	public Response<?> ASYAU40201(@ModelAttribute ASYAU40201DTO input, HttpServletRequest request) throws Exception{
		ASYAU40201DTO output = new ASYAU40201DTO();
		
		/*========================================================
		  - 사용자 권한 정보 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40201(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 권한 정보 저장")
	@RequestMapping(value = "/ASYAU40202.json")
	public Response<?> ASYAU40202(@ModelAttribute ASYAU40202DTO input, HttpServletRequest request) throws Exception{
		ASYAU40202DTO output = new ASYAU40202DTO();
		
		/*========================================================
		  - 사용자 권한 정보 저장 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU40202(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 권한 관리")
	@RequestMapping(value = "/ASYAU50000.view")
	public String ASYAU50000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "사용자 권한 목록 조회")
	@RequestMapping(value = "/ASYAU50001.json")
	public Response<?> ASYAU50001(@ModelAttribute ASYAU50001DTO input,
			@RequestParam(value = "page", required = false) Integer page,
		    @RequestParam(value = "rows", required = false) Integer rows,
			HttpServletRequest request) throws Exception{
		ASYAU50001DTO output = new ASYAU50001DTO();

		if (page == null) {
			page = 1;
		}
		
		if (rows == null) {
			rows = 10;
		}
		
	    input.setPageNo(page);
	    input.setPageSize(rows);
		/*========================================================
		  - 사용자 권한 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU50001(input);
		return Response.ok(output);
	}
	
	@Operation(summary = "사용자 권한 목록 엑셀")
	@RequestMapping(value = "/ASYAU50002.json")
	public Response<?> ASYAU50002(@ModelAttribute ASYAU50002DTO input, HttpServletRequest request) throws Exception{
		ASYAU50002DTO output = new ASYAU50002DTO();
		
		/*========================================================
		  - 사용자 권한 목록 엑셀 서비스 호출
		  ========================================================*/
		output = aSYAUService.ASYAU50002(input);
		return Response.ok(output);
	}

}