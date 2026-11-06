package kr.fasol.obix.admin.biz.admin.sy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import kr.fasol.obix.admin.biz.admin.sy.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import io.swagger.v3.oas.annotations.Operation;
import kr.fasol.obix.admin.biz.admin.sy.service.ASYOPService;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 시스템관리 > 운영관리
 * 프로그램명 : 운영관리
 * 설명 : 
 *
 * 클래스 : ASYOPController
 * 작성자 : 최정환
 * 작성일 : 2023. 03. 28.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 04. 25.        서예원			  배치실행이력조회 추가
 * 2023. 03. 28.        최정환			  최초작성
 */

@Slf4j
@Controller
@RequestMapping(value = "/admin/sy/op")
@Tag(name = "시스템관리 > 운영관리")
public class ASYOPController extends AbstractBaseController {
	
	@Resource(name = "ASYOPService")
	private ASYOPService aSYOPService;
	
	@Operation(summary = "코드 관리")
	@RequestMapping(value = "/ASYOP10000.view")
	public String ASYOP10000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "코드그룹 목록 조회")
	@RequestMapping(value = "/ASYOP10001.json")
	public Response<?> ASYOP10001(@ModelAttribute ASYOP10001DTO input, HttpServletRequest request) throws Exception{
		ASYOP10001DTO output = new ASYOP10001DTO();

		/*========================================================
		  - 코드그룹 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10001(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드 목록 조회")
	@RequestMapping(value = "/ASYOP10002.json")
	public Response<?> ASYOP10002(@ModelAttribute ASYOP10002DTO input, HttpServletRequest request) throws Exception{
		ASYOP10002DTO output = new ASYOP10002DTO();

		/*========================================================
		  - 코드 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10002(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드정보 엑셀")
	@RequestMapping(value = "/ASYOP10003.json")
	public Response<?> ASYOP10003(@ModelAttribute ASYOP10003DTO input, HttpServletRequest request) throws Exception{
		ASYOP10003DTO output = new ASYOP10003DTO();

		/*========================================================
		  - 코드정보 엑셀 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10003(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드정보 서버적용")
	@RequestMapping(value = "/ASYOP10004.json")
	public Response<?> ASYOP10004(@ModelAttribute ASYOP10004DTO input, HttpServletRequest request) throws Exception{
		ASYOP10004DTO output = new ASYOP10004DTO();

		/*========================================================
		  - 코드정보 서버적용 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10004(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드그룹 설정")
	@RequestMapping(value = "/ASYOP10100.frag")
	public String ASYOP10100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "코드그룹 설정정보 조회")
	@RequestMapping(value = "/ASYOP10101.json")
	public Response<?> ASYOP10101(@ModelAttribute ASYOP10101DTO input, HttpServletRequest request) throws Exception{
		ASYOP10101DTO output = new ASYOP10101DTO();

		/*========================================================
		  - 코드그룹 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10101(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드그룹 설정정보 저장")
	@RequestMapping(value = "/ASYOP10102.json")
	public Response<?> ASYOP10102(@ModelAttribute ASYOP10102DTO input, HttpServletRequest request) throws Exception{
		ASYOP10102DTO output = new ASYOP10102DTO();

		/*========================================================
		  - 코드그룹 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10102(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드 설정")
	@RequestMapping(value = "/ASYOP10200.frag")
	public String ASYOP10200(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "코드 설정정보 조회")
	@RequestMapping(value = "/ASYOP10201.json")
	public Response<?> ASYOP10201(@ModelAttribute ASYOP10201DTO input, HttpServletRequest request) throws Exception{
		ASYOP10201DTO output = new ASYOP10201DTO();

		/*========================================================
		  - 코드 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10201(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "코드 설정정보 저장")
	@RequestMapping(value = "/ASYOP10202.json")
	public Response<?> ASYOP10202(@ModelAttribute ASYOP10202DTO input, HttpServletRequest request) throws Exception{
		ASYOP10202DTO output = new ASYOP10202DTO();

		/*========================================================
		  - 코드 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP10202(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "게시판 관리")
	@RequestMapping(value = "/ASYOP20000.view")
	public String ASYOP20000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "게시판 목록 조회")
	@RequestMapping(value = "/ASYOP20001.json")
	public Response<?> ASYOP20001(@ModelAttribute ASYOP20001DTO input, 
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			HttpServletRequest request) throws Exception{
		ASYOP20001DTO output = new ASYOP20001DTO();
		
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 10;
		}
		/*========================================================
		  - 게시판 목록 조회 서비스 호출
		  ========================================================*/
		input.setPageNo(page);
		input.setPageSize(rows);
		
		output = aSYOPService.ASYOP20001(input);
		output.setGridCnt(output.getGrid().size()); // 그리드건수
		
		return Response.ok(output);
	}
	
	@Operation(summary = "게시판 설정")
	@RequestMapping(value = "/ASYOP20100.frag")
	public String ASYOP20100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "게시판 설정정보 조회")
	@RequestMapping(value = "/ASYOP20101.json")
	public Response<?> ASYOP20101(@ModelAttribute ASYOP20101DTO input, HttpServletRequest request) throws Exception{
		ASYOP20101DTO output = new ASYOP20101DTO();

		/*========================================================
		  - 게시판 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP20101(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "게시판 설정정보 저장")
	@RequestMapping(value = "/ASYOP20102.json")
	public Response<?> ASYOP20102(@ModelAttribute ASYOP20102DTO input, HttpServletRequest request) throws Exception{
		ASYOP20102DTO output = new ASYOP20102DTO();

		/*========================================================
		  - 게시판 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP20102(input);
		
		return Response.ok(output);
	}

	@Operation(summary = "배치 작업 관리")
	@RequestMapping(value = "/ASYOP30000.view")
	public String ASYOP30000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "배치 작업 목록 조회")
	@RequestMapping(value = "/ASYOP30001.json")
	public Response<?> ASYOP30001(@ModelAttribute ASYOP30001DTO input,
								  @RequestParam(value="page", required=false) Integer page,
								  @RequestParam(value="rows", required=false) Integer rows,
								  HttpServletRequest request) throws Exception{
		ASYOP30001DTO output = new ASYOP30001DTO();

		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 10;
		}
		/*========================================================
		  - 배치 작업 목록 조회 서비스 호출
		  ========================================================*/
		input.setPageNo(page);
		input.setPageSize(rows);

		output = aSYOPService.ASYOP30001(input);
		output.setGridCnt(output.getGrid().size()); // 그리드건수

		return Response.ok(output);
	}

	@Operation(summary = "배치 서버 적용")
	@RequestMapping(value = "/ASYOP30002.json")
	public Response<?> ASYOP30002(@ModelAttribute ASYOP30002DTO input, HttpServletRequest request) throws Exception{
		ASYOP30002DTO output = new ASYOP30002DTO();

		/*========================================================
		  - 배치 서버 적용 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP30002(input);

		return Response.ok(output);
	}

	@Operation(summary = "배치 작업 설정")
	@RequestMapping(value = "/ASYOP30100.frag")
	public String ASYOP30100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "배치 작업 설정정보 조회")
	@RequestMapping(value = "/ASYOP30101.json")
	public Response<?> ASYOP30101(@ModelAttribute ASYOP30101DTO input, HttpServletRequest request) throws Exception{
		ASYOP30101DTO output = new ASYOP30101DTO();

		/*========================================================
		  - 배치 작업 설정정보 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP30101(input);

		return Response.ok(output);
	}

	@Operation(summary = "배치 작업 설정정보 저장")
	@RequestMapping(value = "/ASYOP30102.json")
	public Response<?> ASYOP30102(@ModelAttribute ASYOP30102DTO input, HttpServletRequest request) throws Exception{
		ASYOP30102DTO output = new ASYOP30102DTO();

		/*========================================================
		  - 배치 작업 설정정보 저장 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP30102(input);

		return Response.ok(output);
	}

	@Operation(summary = "배치 실행 이력")
	@RequestMapping(value = "/ASYOP40000.view")
	public String ASYOP40000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "배치 실행 이력 조회")
	@RequestMapping(value = "/ASYOP40001.json")
	public Response<?> ASYOP40001(@ModelAttribute ASYOP40001DTO input,
								  @RequestParam(value="page", required=false) Integer page,
								  @RequestParam(value="rows", required=false) Integer rows,
								  HttpServletRequest request) throws Exception{
		ASYOP40001DTO output = new ASYOP40001DTO();

		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 10;
		}
		/*========================================================
		  - 배치 실행 이력 조회 서비스 호출
		  ========================================================*/
		input.setPageNo(page);
		input.setPageSize(rows);

		output = aSYOPService.ASYOP40001(input);
		output.setGridCnt(output.getGrid().size()); // 그리드건수

		return Response.ok(output);
	}

	@Operation(summary = "배치 재실행 요청")
	@RequestMapping(value = "/ASYOP40002.json")
	public Response<?> ASYOP40002(@ModelAttribute ASYOP40002DTO input, HttpServletRequest request) throws Exception{
		ASYOP40002DTO output = new ASYOP40002DTO();

		/*========================================================
		  - 배치 재실행 요청 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP40002(input);

		return Response.ok(output);
	}

	@Operation(summary = "시스템 변수 설정 관리")
	@RequestMapping(value = "/ASYOP50000.view")
	public String ASYOP50000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "시스템 변수 설정 목록 조회")
	@RequestMapping(value = "/ASYOP50001.json")
	public Response<?> ASYOP50001(@ModelAttribute ASYOP50001DTO input, HttpServletRequest request) throws Exception{
		ASYOP50001DTO output = new ASYOP50001DTO();

		/*========================================================
		  - 시스템 변수 설정 목록 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP50001(input);
		
		return Response.ok(output);
	}

	@Operation(summary = "시스템 변수 설정")
	@RequestMapping(value = "/ASYOP50100.frag")
	public String ASYOP50100(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}
	
	@Operation(summary = "시스템 변수 설정 정보 조회")
	@RequestMapping(value = "/ASYOP50101.json")
	public Response<?> ASYOP50101(@ModelAttribute ASYOP50101DTO input, HttpServletRequest request) throws Exception{
		ASYOP50101DTO output = new ASYOP50101DTO();

		/*========================================================
		  - 시스템 변수 설정 정보 조회 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP50101(input);
		
		return Response.ok(output);
	}
	
	@Operation(summary = "시스템 변수 설정 정보 저장")
	@RequestMapping(value = "/ASYOP50102.json")
	public Response<?> ASYOP50102(@ModelAttribute ASYOP50102DTO input, HttpServletRequest request) throws Exception{
		ASYOP50102DTO output = new ASYOP50102DTO();

		/*========================================================
		  - 시스템 변수 설정 정보 저장 서비스 호출
		  ========================================================*/
		output = aSYOPService.ASYOP50102(input);
		
		return Response.ok(output);
	}

}
