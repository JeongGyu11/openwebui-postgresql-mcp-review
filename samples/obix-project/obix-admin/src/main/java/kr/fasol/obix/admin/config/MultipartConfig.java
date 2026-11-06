package kr.fasol.obix.admin.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * 설명 : Multipart 설정
 *
 * 클래스 : MultipartConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2023/08/22
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/08/22         정영탁          최초생성
 */
@Configuration
public class MultipartConfig {

    /**
     * @return [MultipartResolver 설정] StandardServletMultipartResolver 등록
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofMegabytes(50L));
        factory.setMaxFileSize(DataSize.ofMegabytes(50L));
        return factory.createMultipartConfig();
    }

}
