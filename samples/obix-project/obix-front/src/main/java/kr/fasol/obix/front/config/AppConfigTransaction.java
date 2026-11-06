package kr.fasol.obix.front.config;

import java.util.Collections;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 시스템명 : 
 * 업무구분명 : 설정 > DB의 Transaction 설정
 * 프로그램명 : DB의 Transaction 설정
 * 설명 :
 *
 * 클래스 : AppConfigTransaction.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 12. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 12. 13.         정영탁          최초생성
 */
@Configuration
public class AppConfigTransaction {

    /**
     * TransactionManager 설정
     */
	@Bean(name={"txManager", "transactionManager"})
	public DataSourceTransactionManager txManager(@Qualifier("dataSource") DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}

	@Bean
	public TransactionInterceptor txAdvice(DataSourceTransactionManager txManager) {
		RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();
		txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

		HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();
		txMethods.put("*", txAttribute);

		NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
		txAttributeSource.setNameMap(txMethods);

		TransactionInterceptor txAdvice = new TransactionInterceptor();
		txAdvice.setTransactionAttributeSource(txAttributeSource);
		txAdvice.setTransactionManager(txManager);

		return txAdvice;
	}

	@Bean
	public Advisor txAdvisor(@Qualifier("txManager") DataSourceTransactionManager txManager) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(
				"execution(* kr.fasol..service.*.*(..))"
		);
		return new DefaultPointcutAdvisor(pointcut, txAdvice(txManager));
	}

}
