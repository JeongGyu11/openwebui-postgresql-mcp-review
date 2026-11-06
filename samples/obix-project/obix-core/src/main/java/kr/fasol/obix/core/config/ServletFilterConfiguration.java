package kr.fasol.obix.core.config;

import kr.fasol.obix.core.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 시스템명 :
 * 업무구분명 : F/W 설정 > Servlet Filter 공통 설정
 * 프로그램명 : Servlet Filter 공통 설정
 * 설명 :
 *
 * 클래스 : ServletFilterConfiguration.java
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
public class ServletFilterConfiguration {

    /**
     * 로깅을 위한 Filter
     */
    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new LoggingFilter());
        filter.addUrlPatterns("/*");
        filter.setOrder(0);
        return filter;
    }

}
