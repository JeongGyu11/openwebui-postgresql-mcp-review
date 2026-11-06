package kr.fasol.obix.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.base.validation.ByteSize;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.enums.AppHeader;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.util.GuidContext;
import kr.fasol.obix.core.util.GuidUtil;
import kr.fasol.obix.core.util.WebUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

/**
 * request별로 정보를 담아놓으려고 생성함
 * 현재는 Guid만 담고 있는데 더 필요한 값이 생긴다면 클래스 명을 변경해야 함.
 */
@Slf4j
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
@Getter
public class BusinessTierHeaderInfoProviderImpl implements BusinessTierHeaderInfoProvider {

    // Header
    @ByteSize(max = 36)
    private final String guid;                          // 전문의 Unique한 고유번호

    @ByteSize(max = 39)
    private final String frstTrnmIpad;                  // 최초 전송 IP 주소
    @ByteSize(max = 3)
    private final String frstTrnmSysCd;                 // 최초 전송 시스템 코드

    // Common
    @ByteSize(max = 4)
    private final String txBrcd;                        // 거래부점코드
    @ByteSize(max = 1)
    private final String chnlTycd;                      // 채널유형코드
    @ByteSize(max = 3)
    private final String chnlDtlsClcd;                  // 채널세부분류코드
    @ByteSize(max = 3)
    private final String langDvcd;                      // 언어구분코드
    // Bypass client app Header.
    private final String deviceId;
    private final String pid;
    @ByteSize(max = 3)
    private final String clphOsDvcd;                    // 휴대전화운영체계 구분코드

    private final String userAgent;
    private final GuidContext guidContext;
    private final LocalDateTime requestDateTime;

    @Autowired
    public BusinessTierHeaderInfoProviderImpl(HttpServletRequest request, GuidContext guidContext) {
        this.guidContext = guidContext;

        // guidContext에 GUID가 없으면 생성하고 저장한다.
        if (StringUtils.isEmpty(this.guidContext.getGuid())) {
            this.guid = StringUtils.defaultIfBlank(MDC.get(LoggingConstant.REQUEST_GUID), GuidUtil.createGuid());
            this.guidContext.setGuid(this.guid);
        } else {
            log.debug("guidContext.getGuid is not empty. guidContext.getGuid(): {}", this.guidContext.getGuid());
            this.guid = this.guidContext.getGuid();
        }
        log.debug("guid: {}", guid);

        // 헤더에서 X-OBIX-IP (L4에서 전달해 준 IP)가 있으면 사용. 없으면 X-Forward-For 사용. 그것도 없으면 RemoteAddr 을 사용한다. (없으면 계정계 통신시 오류 발생)
        this.frstTrnmIpad = StringUtils.defaultIfBlank(getHeader(request, AppHeader.FRST_TRNM_IPAD), StringUtils.defaultIfBlank(getHeader(request, AppHeader.X_FORWARDED_FOR), WebUtil.getClientIp(request)));
        this.frstTrnmSysCd = "OBIX";

        this.txBrcd = "0000";
        this.chnlTycd = "M";    // 채널유형코드(I:인터넷, M:모바일)
        this.chnlDtlsClcd = "BEA";  // 채널세부분류코드(MPA: Front, ADM: 관리자, BEA: Back-End)
        this.langDvcd = StringUtils.defaultIfBlank(getHeader(request, AppHeader.LANG_DVCD),"KOR");;

        this.deviceId = "";
        this.pid = getHeader(request, AppHeader.PID);

        // 휴대전화운영체계 구분코드(IOS, AOS, ETC)
        this.clphOsDvcd = "ETC";

        this.userAgent = getHeader(request, AppHeader.USER_AGENT);
        this.requestDateTime = LocalDateTime.now();
    }

    private String getHeader(HttpServletRequest request, AppHeader appHeader) {
        return request.getHeader(appHeader.getFieldName());
    }

    @Override
    public String getSysOprtEnvDvcd() {
        throw new UnsupportedOperationException("getSysOprtEnvDvcd() method is unsupported.");
    }

    @Override
    public String getTxTypDvcd() {
        throw new UnsupportedOperationException("getTxTypDvcd() method is unsupported.");
    }

    @Override
    public void beforeSend() {
        guidContext.createChildGuid();
    }

    @Override
    public void setGuid(String guid) {
        guidContext.setNextHop(guid);
    }

    @Override
    public String getGuid() {
        return guidContext.getGuid();
    }

    @Override
    public String getChildGuid() {
        return guidContext.getChildGuid();
    }

}
