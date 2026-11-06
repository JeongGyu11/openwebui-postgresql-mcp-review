package kr.fasol.obix.core.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 시스템명 : 
 * 업무구분명 : F/W 설정 > Async Thread 설정
 * 프로그램명 : Async Thread 설정
 * 설명 :
 *
 * 클래스 : AsyncThreadConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 31.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 31.         정영탁          최초생성
 */
public abstract class AsyncThreadConfiguration {

	/**
	 * ThreadPool Task Executor 생성
	 */
	public Executor configThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds, int awaitTerminationSeconds, String threadNamePrefix) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);		// 기본적으로 실행을 대기하고 있는 Thread의 갯수
		executor.setMaxPoolSize(maxPoolSize);		// 동시 동작하는, 최대 Thread 갯수
		executor.setQueueCapacity(queueCapacity);		// MaxPoolSize를 초과하는 요청이 Thread 생성 요청시 해당 내용을 Queue에 저장하게 되고, 사용할 수 있는 Thread 여유 자리가 발생하면 하나씩 꺼내져서 동작하게 된다.
		executor.setKeepAliveSeconds(keepAliveSeconds);	// 하나의 요청에 대한 최대 연결 시간(단위:초)
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());	// shutdown 상태가 아니라면 ThreadPoolTaskExecutor에 요청한 thread에서 직접 처리
		executor.setAllowCoreThreadTimeOut(true);	// KeepAliveSeconds로 설정된 시간만큼 Task가 할당되지 않은 Thread를 core thread로 정리할지 말지 여부 설정
		executor.setWaitForTasksToCompleteOnShutdown(false);	// 종료시 queue에 남아있는 모든 작업이 완료될 때까지 대기할지 여부
		executor.setAwaitTerminationSeconds(awaitTerminationSeconds);	// shutdown 최대 대기시간
		executor.setThreadNamePrefix(threadNamePrefix);	// Spring이 생성하는 Thread의 접두사 지정
		executor.setTaskDecorator(new LoggingTaskDecorator());
		executor.initialize();
		return executor;
	}

    /**
     * Logging을 위한 TaskDecorator 구현체
     */
    public static class LoggingTaskDecorator implements TaskDecorator {
		@Override
		public Runnable decorate(Runnable runnable) {
			Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
			return () -> {
				if (callerThreadContext != null) {
					MDC.setContextMap(callerThreadContext);
				}
				runnable.run();
			};
		}
    }

}
