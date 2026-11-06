package kr.fasol.obix.core.config;

import kr.fasol.obix.core.webe2e.WebE2eSecurityUtil;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 시스템명 : 
 * 업무구분명 : 설정 > Jasypt 암복호화 설정
 * 프로그램명 : Jasypt 암복호화 설정
 * 설명 : 
 *
 * 클래스 : JasyptConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 25.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 25.        정영탁			  최초작성
 */
@Configuration
public class JasyptConfiguration {

	@Bean("jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {
		String password = WebE2eSecurityUtil.getPublicKeyString();
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

}
