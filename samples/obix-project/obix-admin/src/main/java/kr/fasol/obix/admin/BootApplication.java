package kr.fasol.obix.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages="kr.fasol")
public class BootApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BootApplication.class);
	}

	public static void main(String[] args) {

		// JVM Options 설정 체크
		for (String key : Arrays.asList("spring.profiles.active", "application.name", "LOG_FILE_ROOT")) {
			if (StringUtils.isBlank(System.getProperty(key))) {
				throw new RuntimeException("JVM Options의 [" + key + "]가 설정되어 있지 않습니다.");
			}
		}

		SpringApplication springApplication = new SpringApplication(BootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);
	}

}
