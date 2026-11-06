package kr.fasol.obix.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	AppConfigCommon.class,
	AppConfigDatabase.class,
	AppConfigThread.class,
	AppConfigTransaction.class
})
public class AppConfig {

}
