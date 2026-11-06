package kr.fasol.obix.backend.biz.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 시스템명 :
 * 업무구분명 :
 * 프로그램명 :
 * 설명 :
 *
 * 클래스 : ExampleController.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 5.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 5.        정영탁			  최초작성
 */
@Slf4j
@Tag(name = "예제")
@RequestMapping(value = "/example")
@RestController
public class ExampleController extends AbstractBaseController {

    @Operation(summary = "예제")
    @GetMapping(value = "/{menuId}/list")
    public Response<?> variable(@PathVariable(value="menuId") String menuId, HttpServletRequest request) throws Exception {
        log.debug("### MENU_ID = {}", menuId);

        return Response.ok(Map.of("menuId", menuId));
    }

}
