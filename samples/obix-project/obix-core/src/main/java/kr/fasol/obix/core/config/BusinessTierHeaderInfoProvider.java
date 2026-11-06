package kr.fasol.obix.core.config;

import java.time.LocalDateTime;

/**
 * 프로그램명 : Request Header 정보
 */
public interface BusinessTierHeaderInfoProvider {

    /**
     * 전문의 Unique한 고유번호
     */
    String getGuid();

    /**
     * 최초 전송 IP 주소
     */
    String getFrstTrnmIpad();

    /**
     * 최초 전송 시스템 코드
     */
    String getFrstTrnmSysCd();

    /**
     * 시스템 환경 구분코드
     */
    String getSysOprtEnvDvcd();

    /**
     * 거래구분코드
     */
    String getTxTypDvcd();

    /**
     * 거래부점코드
     */
    String getTxBrcd();

    /**
     * 채널유형코드
     */
    String getChnlTycd();

    /**
     * 채널세부분류코드
     */
    String getChnlDtlsClcd();

    /**
     * 언어구분코드
     */
    String getLangDvcd();

    /**
     * 디바이스 ID
     */
    String getDeviceId();

    /**
     * 휴대전화운영체계 구분코드
     * @return
     */
    String getClphOsDvcd();

    /**
     * Hop(3자리)를 포함한 전문 고유번호(GUID)
     */
    String getChildGuid();

    void beforeSend();

    void setGuid(String guid);

    /**
     * 요청일시
     */
    LocalDateTime getRequestDateTime();

    /**
     * user agent
     */
    String getUserAgent();

}
