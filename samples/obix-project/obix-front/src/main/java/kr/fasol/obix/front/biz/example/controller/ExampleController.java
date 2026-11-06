package kr.fasol.obix.front.biz.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.common.dto.entity.CmnCdDTO;
import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.message.HttpClientAdaptor;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.message.impl.HttpClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
@RequestMapping(value = "/example")
@Tag(name = "예제")
public class ExampleController extends AbstractBaseController {

    @Autowired
    private HttpClientAdaptor httpClientAdaptor;

    @Operation(summary = "예제")
    @RequestMapping(value = "/{menuId}.view")
    public String variable(@PathVariable(value="menuId") String menuId, HttpServletRequest request) throws Exception {
        log.debug("### MENU_ID = {}", menuId);

        CmnCdDTO reqDto = CmnCdDTO.newBuilder()
                .cdVal("MENU_ID")
                .cdNm(menuId)
                .build();

        HttpClientResponse<Response> httpClientResponse = httpClientAdaptor.businessTier(HttpClientConstant.Msa.FRONT)
                .post("/api/example/test.json")
                .addHeader("X-OBIX-TEST", "TEST")
                .body(reqDto)
                .call(Response.class);

        Response<?> resBody = httpClientResponse.orElseThrowGet();

        log.debug("### RESULT = {}", resBody);

        return getDefaultViewName(request);
    }

}
