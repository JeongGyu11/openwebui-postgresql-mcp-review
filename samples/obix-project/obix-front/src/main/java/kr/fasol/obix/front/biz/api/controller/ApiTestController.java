package kr.fasol.obix.front.biz.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.common.dto.entity.CmnCdDTO;
import kr.fasol.obix.core.base.AbstractBaseController;
import kr.fasol.obix.core.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/example")
public class ApiTestController extends AbstractBaseController {

    @GetMapping("/test.json")
    public Response<?> getTest(@ModelAttribute CmnCdDTO input, HttpServletRequest request) {
        log.debug("### 입력값 : {}", input);
        return Response.SUCCESS;
    }

    @PostMapping("/test.json")
    public Response<?> postTest(@RequestBody CmnCdDTO input, HttpServletRequest request) {
        log.debug("### 입력값 : {}", input);
        return Response.SUCCESS;
    }

    @PutMapping("/test.json")
    public Response<?> putTest(@RequestBody CmnCdDTO input, HttpServletRequest request) {
        log.debug("### 입력값 : {}", input);
        return Response.SUCCESS;
    }

    @DeleteMapping("/test.json")
    public Response<?> deleteTest(@RequestBody CmnCdDTO input, HttpServletRequest request) {
        log.debug("### 입력값 : {}", input);
        return Response.SUCCESS;
    }

    @PatchMapping("/test.json")
    public Response<?> patchTest(@RequestBody CmnCdDTO input, HttpServletRequest request) {
        log.debug("### 입력값 : {}", input);
        return Response.SUCCESS;
    }

}
