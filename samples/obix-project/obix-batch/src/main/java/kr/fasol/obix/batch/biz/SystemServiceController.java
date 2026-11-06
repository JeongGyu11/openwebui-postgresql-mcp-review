package kr.fasol.obix.batch.biz;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.batch.dao.comm.BtchExeTxnDAO;
import kr.fasol.obix.batch.job.BatchService;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.EnvironmentUtil;
import kr.fasol.obix.core.util.ProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템명 : 배치
 * 업무구분명 : 공통 > 시스템 관리
 * 프로그램명 : 시스템 관리
 * 설명 :
 *
 * 클래스 : SystemServiceController.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 10. 21.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 10. 21.         정영탁          최초생성
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemServiceController {

    @Autowired
    private BatchService batchService;

    @Resource(name = "BtchExeTxnDAO")
    private BtchExeTxnDAO btchExeTxnDAO;

    /**
     * 배치 Job 목록
     */
    @RequestMapping("/jobList.json")
    public Response<?> jobList(HttpServletRequest request) throws Exception {
        List<Map<String, Object>> output = new ArrayList<>();

        List<BtchBasDTO> jobList = batchService.getListBtchBas();
        if (jobList != null) {
            for (BtchBasDTO dto : jobList) {
                Map<String, Object> map = new HashMap<>();
                map.put("btchJobId", dto.getBtchJobId());
                map.put("btchJobNm", dto.getBtchJobNm());
                map.put("btchExePgmNm", dto.getBtchExePgmNm());
                output.add(map);
            }
        }

        return Response.ok(output);
    }

    /**
     * 배치 Job 실행
     */
    @RequestMapping("/jobLauncher.json")
    public Response<?> jobLauncher(@ModelAttribute BtchBasDTO input, HttpServletRequest request) throws Exception {

        BtchBasDTO btchBasDTO = btchExeTxnDAO.selectBtchBas(input);
        if (btchBasDTO == null) {
            throw new UserException("BIZ.BATCH.01").setDebuggingMessage("[등록된 Job 없음] 배치작업ID = " + input.getBtchJobId());
        }

        /*
         * 입력 파라미터 설정
         */
        Map<String, JobParameter<?>> parameters = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            parameters.put(key, new JobParameter<>(request.getParameter(key), String.class));
        }

        // 재실행가능여부
        if (!parameters.containsKey("executeTime")) {
            if ("Y".equals(btchBasDTO.getReExePosblYn())) {
                parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDateTime(), String.class));
            } else {
                parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDate(), String.class));
            }
        }

        batchService.asyncExecuteJobLauncher(btchBasDTO, new JobParameters(parameters));
        return Response.SUCCESS;
    }

    /**
     * Local 실행용 배치 Job 실행
     */
    @RequestMapping("/localJobLauncher.json")
    public Response<?> localJobLauncher(@ModelAttribute BtchBasDTO input, HttpServletRequest request) throws Exception {

        // 로컬 환경에서만 실행
        if (!ProfileUtil.isLocal()) {
            return Response.builder().status("CB1004", "로컬 개발 환경에서만 실행 가능합니다.").build();
        }

        /*
         * 입력 파라미터 설정
         */
        Map<String,JobParameter<?>> parameters = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            parameters.put(key, new JobParameter<>(request.getParameter(key),String.class));
        }
        parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDateTime(), String.class));

        return batchService.executeJobLauncher(input, new JobParameters(parameters));
    }

}
