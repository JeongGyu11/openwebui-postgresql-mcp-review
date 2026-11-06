package kr.fasol.obix.backend.config;

import kr.fasol.obix.backend.common.interceptor.BackendCommonInterceptor;
import kr.fasol.obix.core.handler.CustomHandlerExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
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
		registry.addViewController("/favicon.ico").setViewName("forward:/static/favicon.ico");
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
    }

    /**
     * resource 폴더 캐시 설정
     */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(3600)	// 캐시 처리용(단위:초)
				.setCacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS).mustRevalidate())
				.resourceChain(true)
				.addResolver(new PathResourceResolver())
				.addResolver(new EncodedResourceResolver())
		;

		// Swagger UI 설정
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/static/swagger-ui/");
	}

	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 - Interceptor 추가
	// -------------------------------------------------------------
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 기본 Interceptor 추가
		registry.addInterceptor(new BackendCommonInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/static/**")
				.excludePathPatterns("/error**", "/error/*")
				.excludePathPatterns("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs.*", "/v3/api-docs/**")
			;
	}

}
