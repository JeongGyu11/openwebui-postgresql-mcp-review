package kr.fasol.obix.front.config;

import kr.fasol.obix.core.config.ServletFilterConfiguration;
import kr.fasol.obix.core.filter.XssProtectFilter;
import kr.fasol.obix.core.webe2e.filter.WebE2eFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 설명 : Servlet Filter 설정
 *
 * @Class  ServletFilterConfig.class
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
public class ServletFilterConfig extends ServletFilterConfiguration {

	/**
	 * E2E 암복호화를 위한 Filter
	 */
	@Bean
	public FilterRegistrationBean<WebE2eFilter> webE2eFilter() {
		FilterRegistrationBean<WebE2eFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new WebE2eFilter());
		filter.addUrlPatterns("*.do", "*.view", "*.frag", "*.indv", "*.blnk", "*.file", "*.excl", "*.cmd", "*.json", "*.xml");
		filter.setOrder(1);
		return filter;
	}

	/**
	 * Spring CharacterEncodingFilter 설정
	 */
	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> encodingFilter() {
		FilterRegistrationBean<CharacterEncodingFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new CharacterEncodingFilter());
		filter.addInitParameter("encoding", "UTF-8");
		filter.addInitParameter("forceEncoding", "true");
		filter.addUrlPatterns("*.do", "*.view", "*.frag", "*.indv", "*.blnk", "*.file", "*.excl", "*.cmd", "*.json", "*.xml");
		filter.setOrder(2);
		return filter;
	}

	/**
	 * XSS(크로스 사이트 스크립트) Filter
	 */
	@Bean
	public FilterRegistrationBean<XssProtectFilter> xssProtectFilter() {
		FilterRegistrationBean<XssProtectFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new XssProtectFilter());
		filter.addUrlPatterns("*.do", "*.view", "*.frag", "*.indv", "*.blnk", "*.file", "*.excl", "*.cmd", "*.json", "*.xml");
		filter.setOrder(3);
		return filter;
	}

	/*
	 * 서버에서 ErrorPageFilter 에러처리(Disabled 설정)
	 */
	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}
	@Bean
	public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
		FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

}
