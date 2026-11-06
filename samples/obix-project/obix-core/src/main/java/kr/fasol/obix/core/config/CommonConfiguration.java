package kr.fasol.obix.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.google.common.base.CaseFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.AntPathMatcher;

import java.io.Serial;

/**
 * 시스템명 :
 * 업무구분명 : F/W 설정 > 공통 설정
 * 프로그램명 : 공통 설정
 * 설명 :
 *
 * 클래스 : CommonConfiguration.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 20.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 20         정영탁          최초생성
 * --------------------------------------------------------------------
 */
@Configuration
public class CommonConfiguration {

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames(
                "classpath:/META-INF/i18n/messages"	// 다국어 메시지
        );
        reloadableResourceBundleMessageSource.setCacheSeconds(60);
        return reloadableResourceBundleMessageSource;
    }

    /**
     * @return [Resource 설정] 메세지 소스 등록
     */
    @Bean
    public MessageSourceFileAccessor messageSourceFileAccessor() {
        MessageSourceFileAccessor messageSource = new MessageSourceFileAccessor();
        messageSource.setReloadableResourceBundleMessageSource(messageSource());
        return messageSource;
    }

    /**
     * ObjectMapper 설정
     */
    @Bean(name = {"objectMapper"})
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .propertyNamingStrategy(new PropertyNamingStrategy() {
                    @Serial
                    private static final long serialVersionUID = -1690409299160459790L;
                    @Override
                    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
                        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, defaultName);
                    }
                })
                .build();
    }

}
