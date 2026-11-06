package kr.fasol.obix.front.common.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import kr.fasol.obix.front.common.thymeleaf.processor.*;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Thymeleaf Custom Tag > dialect 설정
 * 프로그램명 : dialect 설정
 * 설명 : 
 *
 * 클래스 : ObixDialect.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 8. 4.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 8. 4.        정영탁			  최초작성
 */
public class ObixDialect extends AbstractProcessorDialect {
	private static final String PREFIX = "obix";

	public ObixDialect() {
		super("Obix Dialect" // dialect name
				, PREFIX// 접두사. 속성 사용방법: hello:*
				, 1000 // dialect 우선순위
		);
	}

	/**
	 * dialect 처리기를 초기화 한다.
	 */
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<>();
		processors.add(new CommonCodeListTagProcessor(dialectPrefix));
		processors.add(new CommonCodeTagProcessor(dialectPrefix));
		processors.add(new CurrentDateTagProcessor(dialectPrefix));
		processors.add(new CurrentDateTimeTagProcessor(dialectPrefix));
		processors.add(new FormatAcnoTagProcessor(dialectPrefix));
		processors.add(new FormatBisnoTagProcessor(dialectPrefix));
		processors.add(new FormatDateTagProcessor(dialectPrefix));
		processors.add(new FormatEmailTagProcessor(dialectPrefix));
		processors.add(new FormatInterestTagProcessor(dialectPrefix));
		processors.add(new FormatMoneyTagProcessor(dialectPrefix));
		processors.add(new FormatNumberTagProcessor(dialectPrefix));
		processors.add(new FormatPasswordTagProcessor(dialectPrefix));
		processors.add(new FormatPhoneTagProcessor(dialectPrefix));
		processors.add(new FormatRnnoTagProcessor(dialectPrefix));

		processors.add(new FormSelectStndCodeTagProcessor(dialectPrefix));

		processors.add(new HeadTitleAreaTagProcessor(dialectPrefix));
		processors.add(new LocationAreaTagProcessor(dialectPrefix));
		processors.add(new PageHelperTagProcessor(dialectPrefix));
		processors.add(new RequestDataTagProcessor(dialectPrefix));
		return processors;
	}

}
