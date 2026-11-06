package kr.fasol.obix.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.exception.BaseException;
import kr.fasol.obix.core.util.WasUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.net.InetAddress;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Exception > Exception 정보에 기타정보 추가
 * 프로그램명 : Exception 정보에 기타정보 추가
 * 설명 : 
 *
 * @Class  DefaultAddionalErrorInformationMaker.class
 * @author  정영탁
 * @since   2017-08-01
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-01        정영탁    최초생성
 */
@Slf4j
public class DefaultAddionalErrorInformationMaker {

    private String serverInfo = "";

    public DefaultAddionalErrorInformationMaker() {
        String address = "";
        String containerName = null;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
            if (address != null) {
                String[] adrs = address.split("\\.");
                if ((adrs != null) && (adrs.length > 0)) {
                    address = adrs[0] + "." + adrs[(adrs.length - 1)];
                }
            }
            containerName = WasUtil.getContainerName();
            if ((containerName != null) && (containerName.length() > 2)) {
                containerName = containerName.substring(containerName.length() - 2);
            }
        } catch (Exception e) {
            log.error("[DefaultAddionalErrorInformationMaker]", e);
        }

        if (StringUtils.isNotBlank(address)) {
            this.serverInfo = address;
        }

        if (containerName != null) {
            this.serverInfo = this.serverInfo + "-" + containerName;
        }
    }

    /**
     * Exception 정보에 프로그램정보 추가
     */
    public ExceptionResultVO setAdditionalErrorInfo(HttpServletRequest request, Exception e, ExceptionResultVO exceptionResultVO) {
        log.trace("START ADDITIONAL ERROR INFO");
        exceptionResultVO.setServerInfo(this.serverInfo);

    	StackTraceElement[] se = e.getStackTrace();
        if ((se != null) && (se.length > 0)) {
            exceptionResultVO.setOrgClassName(se[0].getClassName());
            exceptionResultVO.setOrgMethodName(se[0].getMethodName());
            exceptionResultVO.setOrgLineNumber(se[0].getLineNumber());
        	exceptionResultVO.setProgramLine(String.valueOf(se[0].getLineNumber()));
        }

        String lastClassName = "";
        int lastLineNumber = -1;
        String methodName = "";
        for (StackTraceElement st : se) {
        	if (st.getClassName().contains(exceptionResultVO.getBaseBizClass())) {
        		lastLineNumber = st.getLineNumber();
        		lastClassName = st.getClassName();
        		methodName = st.getMethodName();
        		log.trace("FIND ORIGIN BIZ CLASS : {}", lastClassName);
        		break;
        	}
        }

        exceptionResultVO.setBizClassName(lastClassName);
        exceptionResultVO.setBizLineNumber(lastLineNumber);
        exceptionResultVO.setBizMethodName(methodName);

        if (e instanceof BaseException) {
            exceptionResultVO.setExceptionCause(((BaseException) e).getExceptionCause());
            exceptionResultVO.setExceptionCommon(((BaseException) e).getExceptionCommon());
        }

        String serviceId = MDC.get(LoggingConstant.SERVICE_ID);
        String className = null;
        if (lastLineNumber != -1) {
        	className = exceptionResultVO.getBizClassName();
        } else {
        	className = StringUtils.trimToEmpty(exceptionResultVO.getOrgClassName());
        }
        String[] classname = className.split("\\.");
        String programName = serviceId + ":" + classname[(classname.length - 1)];
        exceptionResultVO.setProgramName(programName);

        log.trace("ProgramName : {}", exceptionResultVO.getProgramName());
        log.trace("ProgramLine : {}", exceptionResultVO.getProgramLine());
        log.trace("SERVER INFO : {}", exceptionResultVO.getServerInfo());
        log.trace("END ADDITIONAL ERROR INFO");

        return exceptionResultVO;
    }

}
