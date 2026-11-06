package kr.fasol.obix.admin.biz;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import kr.fasol.obix.common.dto.entity.SysStupVarTxnDTO;
import kr.fasol.obix.admin.biz.common.service.SysStupService;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : F/W > 공통 > Spring 어플리케이션 시작 서비스
 * 프로그램명 : Spring 어플리케이션 시작 서비스
 * 설명 : Spring Framework 어플리케이션이 구동되었을 때, 최초로 실행할 서비스 구현
 *
 * 클래스 : StartupService.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 2.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 2.         정영탁          최초생성
 */
@Slf4j
@Service
public class StartupService implements InitializingBean, CommandLineRunner, ApplicationRunner {

	@Autowired
	private SysStupService sysStupService;

	/**
	 * InitializingBean 인터페이스 구현 (Bean의 초기화 작업을 위한 인터페이스)
	 * - Bean의 모든 프로퍼티가 설정된 후 추가 초기화 작업 수행
	 * - @PostConstruct 어노테이션이나 설정 메타데이터의 init-method를 사용하는 것이 권장됨
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("### [어플리케이션 초기화][Bean초기화완료]");

		// JVM Options 설정 체크
		Arrays.asList("spring.profiles.active", "application.name", "LOG_FILE_ROOT").forEach((key) -> {
			if (StringUtils.isBlank(System.getProperty(key))) {
				throw new RuntimeException("JVM Options의 [" + key + "]가 설정되어 있지 않습니다.");
			}
		});

		log.info("### [VM arguments] spring.profiles.active = {}", System.getProperty("spring.profiles.active"));
		log.info("### [VM arguments] application.name = {}", System.getProperty("application.name"));
		log.info("### [VM arguments] LOG_FILE_ROOT = {}", System.getProperty("LOG_FILE_ROOT"));

		/*
		 * 시스템설정변수내역 조회
		 */
		List<SysStupVarTxnDTO> sysStupList = sysStupService.getListSysStupVarTxn();
		if (sysStupList != null && !sysStupList.isEmpty()) {
			for (SysStupVarTxnDTO dto : sysStupList) {
				if (log.isDebugEnabled()) {
					log.debug("### [시스템설정변수] {} = {}", dto.getStupVarKey(), dto.getDescCtnt());
				}
				// 시스템 변수에 등록
				System.setProperty(dto.getStupVarKey(), dto.getStupVarVal());
			}
		}

	}

	/**
	 * CommandLineRunner 인터페이스 구현
	 * - 애플리케이션 시작 시 간단한 명령줄 작업 수행
	 */
	@Override
	public void run(String... args) throws Exception {
		log.info("### [어플리케이션 초기화][CommandLineRunner 자동실행]");

		//TODO 어플리케이션 구동 후 자동실행 로직을 여기에 구현

	}

	/**
	 * ApplicationRunner 인터페이스 구현
	 * - Spring Boot 애플리케이션이 완전히 시작된 후 실행되는 인터페이스
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("### [어플리케이션 초기화][ApplicationRunner 자동실행]");

		//TODO 어플리케이션 구동 후 자동실행 로직을 여기에 구현

	}

}
