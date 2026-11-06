package kr.fasol.obix.backend.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 보안
 * 프로그램명 : 
 * 설명 : 
 *
 * 클래스 : WebSecurityConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 19.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 19.        정영탁			  최초작성
 */
@Configuration
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.csrf(CsrfConfigurer::disable)
				.formLogin(Customizer.withDefaults())
				.authorizeHttpRequests(authorizeRequest ->
						authorizeRequest
								.anyRequest().permitAll()
				)
				.headers(
						headersConfigurer ->
								headersConfigurer
										.frameOptions(
												HeadersConfigurer.FrameOptionsConfig::sameOrigin
										)
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) ->
				web
						.httpFirewall(new DefaultHttpFirewall())
						.ignoring()
						.requestMatchers(
								PathRequest.toStaticResources().atCommonLocations()
						);
	}

}
