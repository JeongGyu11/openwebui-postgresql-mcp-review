package kr.fasol.obix.admin.biz.admin.cm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import io.swagger.v3.oas.annotations.Operation;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10101DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10102DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10201DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10301DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10302DTO;
import kr.fasol.obix.admin.biz.admin.cm.service.ACMMNService;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 메인
 * 프로그램명 : 메인
 * 설명 : 
 *
 * 클래스 : ACMMNController.java
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
@RequestMapping(value = "/admin/cm/mn")
@Tag(name = "공통 > 메인")
public class ACMMNController extends AbstractBaseController {

	/** ACMMNService */
	@Resource(name = "ACMMNService")
	private ACMMNService aCMMNService;


	@Operation(summary = "사용자 정보")
	@RequestMapping(value = "/ACMMN10100.frag")
	public String ACMSO10000(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

	@Operation(summary = "사용자 정보 조회")
	@RequestMapping(value = "/ACMMN10101.json")
	public Response<?> ACMMN10101(@ModelAttribute ACMMN10101DTO input, HttpServletRequest request) throws Exception {

		ACMMN10101DTO res = aCMMNService.ACMMN10101(input);

		return Response.ok(res);
	}

	@Operation(summary = "사용자 비밀번호 변경")
    @RequestMapping("/ACMMN10102.json")
    public Response<?> ACMMN10102(ACMMN10102DTO req) throws Exception {
        int res = aCMMNService.ACMMN10102(req);
        
        return Response.ok(res);
    }

	@Operation(summary = "즐겨찾기 추가")
    @RequestMapping("/ACMMN10200.frag")
    public String ACMMN10200(HttpServletRequest req) throws Exception {
        return getDefaultViewName(req);
    }
    
	@Operation(summary = "즐겨찾기 추가 처리")
    @RequestMapping("/ACMMN10201.json")
    public Response<?> ACMMN10201(@RequestBody ACMMN10201DTO req) throws Exception {
    	req.setAllUserId(AdminSessionHelper.getAdminUserId());
    	aCMMNService.ACMMN10201(req);
    	
    	return Response.SUCCESS;
    }
        
	@Operation(summary = "즐겨찾기 관리")
    @RequestMapping("/ACMMN10300.frag")
    public String ACMMN10300(HttpServletRequest req) throws Exception {
        return getDefaultViewName(req);
    }
    
	@Operation(summary = "즐겨찾기 정보 조회")
    @RequestMapping("/ACMMN10301.json")
    public Response<ACMMN10301DTO> ACMMN10301(ACMMN10301DTO req) throws Exception {
    	req.setUserId(AdminSessionHelper.getAdminUserId());
    	ACMMN10301DTO res = aCMMNService.ACMMN10301(req);
    	
        return Response.ok(res);
    }
    
	@Operation(summary = "즐겨찾기 정보 저장")
    @RequestMapping("/ACMMN10302.json")
    public Response<?> ACMMN10302(@RequestBody ACMMN10302DTO req) throws Exception {
    	req.setAlUserId(AdminSessionHelper.getAdminUserId());
    	aCMMNService.ACMMN10302(req);
    	
		return Response.SUCCESS;
    }

}
