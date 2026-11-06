package kr.fasol.obix.core.message.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.enums.AppHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * BT 호출을 위한 Interceptor 설정 (Reuqest Header 정의)
 */
@Slf4j
@Component
public class BusinessTierHeaderBindInterceptor implements RequestInterceptor {

    @Autowired
    private BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;

    @Override
    public void apply(RequestTemplate template) {
        try {
            log.debug("### FeignHeaderBindInterceptor");
            businessTierHeaderInfoProvider.beforeSend();

            setHeader(template, AppHeader.GUID, businessTierHeaderInfoProvider.getChildGuid());
            setHeader(template, AppHeader.FRST_TRNM_SYS_CD, businessTierHeaderInfoProvider.getFrstTrnmSysCd());

            setHeader(template, AppHeader.TX_BRCD, businessTierHeaderInfoProvider.getTxBrcd());
            setHeader(template, AppHeader.CHNL_TYCD, businessTierHeaderInfoProvider.getChnlTycd());
            setHeader(template, AppHeader.CHNL_DTLS_CLCD, businessTierHeaderInfoProvider.getChnlDtlsClcd());
            setHeader(template, AppHeader.LANG_DVCD, businessTierHeaderInfoProvider.getLangDvcd());
            setHeader(template, AppHeader.DEVICE_ID, businessTierHeaderInfoProvider.getDeviceId());

            template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        log.debug("request before send to bt = {}", template);
    }

    private void setHeaderIfNotNull(RequestTemplate template, AppHeader appHeader, String value) {
        if (StringUtils.isNotEmpty(value)) {
            setHeader(template, appHeader, value);
        }
    }

    private void setHeader(RequestTemplate template, AppHeader appHeader, String value) {
        template.header(appHeader.getFieldName(), value);
    }
}
