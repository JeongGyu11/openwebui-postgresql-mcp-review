package kr.fasol.obix.front.config;

import kr.fasol.obix.front.common.thymeleaf.dialect.ObixDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * 프로그램명 : Thymeleaf Resolver 설정
 * 설명 : 
 *
 * 클래스 : ThymeleafConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 4.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 4.        정영탁			  최초작성
 */
@Configuration
public class ThymeleafConfig {

	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setEnableSpringELCompiler(true); // Compiled SpringEL should speed up executions
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new LayoutDialect()); // Thymeleaf Layout Dialect 설정 (JSP의 SiteMesh와 유사한 기능)
		templateEngine.addDialect(new ObixDialect()); // 커스텀 Dialect 추가
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setExcludedViewNames(new String[] { "/swagger-ui/**" }); // Swagger UI 경로 무시
		viewResolver.setOrder(1);
		return viewResolver;
	}

}
