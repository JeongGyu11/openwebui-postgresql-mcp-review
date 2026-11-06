package kr.fasol.obix.batch.config;

import kr.fasol.obix.core.handler.CustomHandlerExceptionResolver;
import kr.fasol.obix.core.view.CustomMappingJackson2HttpMessageConverter;
import kr.fasol.obix.core.view.CustomMappingJackson2JsonView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 설명 : Web MVC 설정
 *
 * @Class  WebMvcConfig.class
 * @author  정영탁
 * @since   2017. 7. 15.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017. 7. 15.        정영탁           최초생성
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	// =====================================================================
	// RequestMappingHandlerMapping 설정
	// =====================================================================

	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 View Controller 추가
	// -------------------------------------------------------------
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter rmha = super.createRequestMappingHandlerAdapter();
		return rmha;
	}

	// -------------------------------------------------------------
	// HandlerExceptionResolver 설정
	// -------------------------------------------------------------
    /**
     * 예외상황(Exception)을 처리해 주는 Resolver 설정
     */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        CustomHandlerExceptionResolver customHandlerExceptionResolver = new CustomHandlerExceptionResolver();
        customHandlerExceptionResolver.setDefaultErrorView("/error/error");
        resolvers.add(customHandlerExceptionResolver);
	}

	// -------------------------------------------------------------
	// View Resolver 설정
	// -------------------------------------------------------------
    /**
     * ViewResolver 설정
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // JSON View
    	CustomMappingJackson2JsonView jsonView = new CustomMappingJackson2JsonView();
        // ContentNegotiation 설정
        registry.enableContentNegotiation(jsonView);
    }

    /**
     * Response 메시지 Type Converter
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	converters.add(customMappingJackson2HttpMessageConverterConfig());
    	converters.add(new ResourceHttpMessageConverter());
    }
    @Bean
    public HttpMessageConverter<?> customMappingJackson2HttpMessageConverterConfig() {
    	CustomMappingJackson2HttpMessageConverter converter = new CustomMappingJackson2HttpMessageConverter();
    	return converter;
    }

    /**
     * 컨텐츠 MediaType 설정
     */
	@SuppressWarnings("deprecation")
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    	configurer.favorParameter(true)
    		.favorPathExtension(true)
	        .parameterName("mediaType")
	        .ignoreAcceptHeader(true)
	        .defaultContentType(MediaType.TEXT_HTML)
	        .mediaType("do", MediaType.TEXT_HTML)
	        .mediaType("view", MediaType.TEXT_HTML)
	        .mediaType("frag", MediaType.TEXT_HTML)
	        .mediaType("indv", MediaType.TEXT_HTML)
	        .mediaType("blnk", MediaType.TEXT_HTML)
	        .mediaType("file", MediaType.APPLICATION_OCTET_STREAM)
	        .mediaType("excl", MediaType.APPLICATION_OCTET_STREAM)
	        .mediaType("cmd", MediaType.APPLICATION_JSON)
	        .mediaType("json", MediaType.APPLICATION_JSON)
	        .mediaType("xml", MediaType.APPLICATION_XML);
		;
	}

	/**
	 * Interceptor
	 */
	@Bean
	public WebContentInterceptor webContentInterceptor() {
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.setCacheSeconds(-1); // 캐시 처리용
		interceptor.setCacheControl(CacheControl.noCache());
		return interceptor;
	}

    /**
     * resource 폴더 캐시 설정
     */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(3600)	// 캐시 처리용(단위:초)
				.setCacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS).mustRevalidate())
				.resourceChain(true)
				.addResolver(new PathResourceResolver())
				.addResolver(new EncodedResourceResolver())
        ;
	}

	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 - Interceptor 추가
	// -------------------------------------------------------------
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	}

}
