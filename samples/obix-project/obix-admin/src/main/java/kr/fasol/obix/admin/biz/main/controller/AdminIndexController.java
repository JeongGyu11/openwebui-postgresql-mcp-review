package kr.fasol.obix.admin.biz.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.fasol.obix.core.base.AbstractBaseController;

/**
 * 시스템명 : 
 * 업무구분명 : 관리자 > 메인
 * 프로그램명 : 
 * 설명 : 
 *
 * 클래스 : AdminIndexController.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 18.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 18.        정영탁			  최초작성
 */
@Controller
@RequestMapping(value = "/admin")
@Tag(name = "관리자 메인")
public class AdminIndexController  extends AbstractBaseController {

	@Operation(summary = "메인")
	@RequestMapping(value = "/main/main.view")
	public String main(HttpServletRequest request) throws Exception {
		return getDefaultViewName(request);
	}

}
