package kr.fasol.obix.core.message.config;

import kong.unirest.HttpRequest;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.enums.AppHeader;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Http Client 호출 서비스의 기본 설정
 */
@Slf4j
@UtilityClass
public class HttpClientDefaultConfig {

    public void setDefaultHeader(BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider, HttpRequest<?> httpRequest) {
        log.debug("### HttpClientDefaultConfig");
        businessTierHeaderInfoProvider.beforeSend();

        httpRequest.header(AppHeader.GUID.getFieldName(), businessTierHeaderInfoProvider.getChildGuid());
        httpRequest.header(AppHeader.FRST_TRNM_SYS_CD.getFieldName(), businessTierHeaderInfoProvider.getFrstTrnmSysCd());

        httpRequest.header(AppHeader.TX_BRCD.getFieldName(), businessTierHeaderInfoProvider.getTxBrcd());
        httpRequest.header(AppHeader.CHNL_TYCD.getFieldName(), businessTierHeaderInfoProvider.getChnlTycd());
        httpRequest.header(AppHeader.CHNL_DTLS_CLCD.getFieldName(), businessTierHeaderInfoProvider.getChnlDtlsClcd());
        httpRequest.header(AppHeader.LANG_DVCD.getFieldName(), businessTierHeaderInfoProvider.getLangDvcd());
        httpRequest.header(AppHeader.DEVICE_ID.getFieldName(), businessTierHeaderInfoProvider.getDeviceId());

        httpRequest.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    public void setDefaultHeader(BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider, HttpHeaders httpHeaders) {
        log.debug("### HttpClientDefaultConfig");
        businessTierHeaderInfoProvider.beforeSend();

        httpHeaders.add(AppHeader.GUID.getFieldName(), businessTierHeaderInfoProvider.getChildGuid());
        httpHeaders.add(AppHeader.FRST_TRNM_SYS_CD.getFieldName(), businessTierHeaderInfoProvider.getFrstTrnmSysCd());

        httpHeaders.add(AppHeader.TX_BRCD.getFieldName(), businessTierHeaderInfoProvider.getTxBrcd());
        httpHeaders.add(AppHeader.CHNL_TYCD.getFieldName(), businessTierHeaderInfoProvider.getChnlTycd());
        httpHeaders.add(AppHeader.CHNL_DTLS_CLCD.getFieldName(), businessTierHeaderInfoProvider.getChnlDtlsClcd());
        httpHeaders.add(AppHeader.LANG_DVCD.getFieldName(), businessTierHeaderInfoProvider.getLangDvcd());
        httpHeaders.add(AppHeader.DEVICE_ID.getFieldName(), businessTierHeaderInfoProvider.getDeviceId());

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

}
