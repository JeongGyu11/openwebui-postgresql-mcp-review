package kr.fasol.obix.batch.config.scheduling;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.*;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 설명 : 배치 스케쥴러 등록을 위한 ScheduledAnnotationBeanPostProcessor를 상속받는 Custom 클래스
 * - Cron 스케쥴러에 파라미터를 추가할 수 있도록 수정
 *
 * @Class  CustomScheduledAnnotationBeanPostProcessor.java
 * @author  정영탁
 * @since   2023. 3. 16.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 3. 16.        정영탁          최초생성
 */
@Slf4j
@Component
public class CustomScheduledAnnotationBeanPostProcessor extends ScheduledAnnotationBeanPostProcessor {

	// @formatter:off
	public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
	private static final boolean reactiveStreamsPresent = org.springframework.util.ClassUtils.isPresent("org.reactivestreams.Publisher", ScheduledAnnotationBeanPostProcessor.class.getClassLoader());
	private final ScheduledTaskRegistrar registrar;
	@Setter @Nullable
	private Object scheduler;
	@Setter @Nullable
	private StringValueResolver embeddedValueResolver;
	@Setter @Nullable
	private String beanName;
	@Setter @Nullable
	private BeanFactory beanFactory;
	@Nullable
	private ApplicationContext applicationContext;
	@Nullable
	private TaskSchedulerRouter localScheduler;
	private final Set nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
	private final Map<Object, Set<ScheduledTask>> scheduledTasks = new IdentityHashMap<>(16);
	private final Map<Object, List<Runnable>> reactiveSubscriptions = new IdentityHashMap<>(16);

	public CustomScheduledAnnotationBeanPostProcessor() {
		this.registrar = new ScheduledTaskRegistrar();
	}

	public CustomScheduledAnnotationBeanPostProcessor(ScheduledTaskRegistrar registrar) {
		Assert.notNull(registrar, "ScheduledTaskRegistrar must not be null");
		this.registrar = registrar;
	}

	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		if (this.beanFactory == null) {
			this.beanFactory = applicationContext;
		}
	}

	public void afterSingletonsInstantiated() {
		this.nonAnnotatedClasses.clear();
		if (this.applicationContext == null) {
			this.finishRegistration();
		}

	}

	private void finishRegistration() {
		if (this.scheduler != null) {
			this.registrar.setScheduler(this.scheduler);
		} else {
			this.localScheduler = new TaskSchedulerRouter();
			this.localScheduler.setBeanName(this.beanName);
			this.localScheduler.setBeanFactory(this.beanFactory);
			this.registrar.setTaskScheduler(this.localScheduler);
		}

		BeanFactory var2 = this.beanFactory;
		if (var2 instanceof ListableBeanFactory lbf) {
			Map<String, SchedulingConfigurer> beans = lbf.getBeansOfType(SchedulingConfigurer.class);
			List<SchedulingConfigurer> configurers = new ArrayList<>(beans.values());
			AnnotationAwareOrderComparator.sort(configurers);

			for(SchedulingConfigurer configurer: configurers){
				configurer.configureTasks(this.registrar);
			}
		}

		this.registrar.afterPropertiesSet();
	}
	// @formatter:on

	private void processScheduledSync(Scheduled scheduled, Method method, Object bean, Object... args) {
		Runnable task;
		try {
			task = this.createRunnable(bean, method, scheduled.scheduler(), args);
		} catch (IllegalArgumentException e) {
			String var10002 = method.getName();
			throw new IllegalStateException("Could not create recurring task for @Scheduled method '" + var10002 + "': " + e.getMessage());
		}

		this.processScheduledTask(scheduled, task, method, bean);
	}

	@SuppressWarnings("unchecked")
	private void processScheduledTask(Scheduled scheduled, Runnable runnable, Method method, Object bean) {
		try {
			boolean processedSchedule = false;
			String errorMessage = "Exactly one of the 'cron', 'fixedDelay' or 'fixedRate' attributes is required";
			Set<ScheduledTask> tasks = new LinkedHashSet<>(4);
			Duration initialDelay = toDuration(scheduled.initialDelay(), scheduled.timeUnit());
			String initialDelayString = scheduled.initialDelayString();
			if (StringUtils.hasText(initialDelayString)) {
				Assert.isTrue(initialDelay.isNegative(), "Specify 'initialDelay' or 'initialDelayString', not both");
				if (this.embeddedValueResolver != null) {
					initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
				}

				if (StringUtils.hasLength(initialDelayString)) {
					try {
						initialDelay = toDuration(initialDelayString, scheduled.timeUnit());
					} catch (RuntimeException e) {
						throw new IllegalArgumentException("Invalid initialDelayString value \"" + initialDelayString + "\"; " + e);
					}
				}
			}

			String cron = scheduled.cron();
			if (StringUtils.hasText(cron)) {
				String zone = scheduled.zone();
				if (this.embeddedValueResolver != null) {
					cron = this.embeddedValueResolver.resolveStringValue(cron);
					zone = this.embeddedValueResolver.resolveStringValue(zone);
				}

				if (StringUtils.hasLength(cron)) {
					Assert.isTrue(initialDelay.isNegative(), "'initialDelay' not supported for cron triggers");
					processedSchedule = true;
					if (!"-".equals(cron)) {
						CronTrigger trigger;
						if (StringUtils.hasText(zone)) {
							trigger = new CronTrigger(cron, StringUtils.parseTimeZoneString(zone));
						} else {
							trigger = new CronTrigger(cron);
						}

						tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, trigger)));
					}
				}
			}

			Duration delayToUse = initialDelay.isNegative() ? Duration.ZERO : initialDelay;
			Duration fixedDelay = toDuration(scheduled.fixedDelay(), scheduled.timeUnit());
			if (!fixedDelay.isNegative()) {
				Assert.isTrue(!processedSchedule, errorMessage);
				processedSchedule = true;
				tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, delayToUse)));
			}

			String fixedDelayString = scheduled.fixedDelayString();
			if (StringUtils.hasText(fixedDelayString)) {
				if (this.embeddedValueResolver != null) {
					fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
				}

				if (StringUtils.hasLength(fixedDelayString)) {
					Assert.isTrue(!processedSchedule, errorMessage);
					processedSchedule = true;

					try {
						fixedDelay = toDuration(fixedDelayString, scheduled.timeUnit());
					} catch (RuntimeException e) {
						throw new IllegalArgumentException("Invalid fixedDelayString value \"" + fixedDelayString + "\"; " + e);
					}

					tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, delayToUse)));
				}
			}

			Duration fixedRate = toDuration(scheduled.fixedRate(), scheduled.timeUnit());
			if (!fixedRate.isNegative()) {
				Assert.isTrue(!processedSchedule, errorMessage);
				processedSchedule = true;
				tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, delayToUse)));
			}

			String fixedRateString = scheduled.fixedRateString();
			if (StringUtils.hasText(fixedRateString)) {
				if (this.embeddedValueResolver != null) {
					fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
				}

				if (StringUtils.hasLength(fixedRateString)) {
					Assert.isTrue(!processedSchedule, errorMessage);
					processedSchedule = true;

					try {
						fixedRate = toDuration(fixedRateString, scheduled.timeUnit());
					} catch (RuntimeException e) {
						throw new IllegalArgumentException("Invalid fixedRateString value \"" + fixedRateString + "\"; " + e);
					}

					tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, delayToUse)));
				}
			}

			if (!processedSchedule) {
				if (initialDelay.isNegative()) {
					throw new IllegalArgumentException("One-time task only supported with specified initial delay");
				}

				tasks.add(this.registrar.scheduleOneTimeTask(new OneTimeTask(runnable, delayToUse)));
			}

			synchronized(this.scheduledTasks) {
				Set regTasks = (Set)this.scheduledTasks.computeIfAbsent(bean, (key) -> {
					return new LinkedHashSet<>(4);
				});
				regTasks.addAll(tasks);
			}
		} catch (IllegalArgumentException e) {
			String var10002 = method.getName();
			throw new IllegalStateException("Encountered invalid @Scheduled method '" + var10002 + "': " + e.getMessage());
		}
	}

	/**
	 * Create a {@link Runnable} for the given bean instance,
	 * calling the specified scheduled method.
	 * <p>The default implementation creates a {@link ScheduledMethodRunnable}.
	 * @param target the target bean instance
	 * @param method the scheduled method to call
	 * @since 5.1
	 * @see ScheduledMethodRunnable#ScheduledMethodRunnable(Object, Method)
	 */
	protected Runnable createRunnable(Object target, Method method, Object... args) {
		//Assert.isTrue(method.getParameterCount() == 0, "Only no-arg methods may be annotated with @Scheduled");
		Method invocableMethod = AopUtils.selectInvocableMethod(method, target.getClass());
		//TODO [Custom] CronTask 호출 시 파라미터 추가
		return new CustomScheduledMethodRunnable(target, invocableMethod, args);
	}

	private static Duration toDuration(long value, TimeUnit timeUnit) {
		try {
			return Duration.of(value, timeUnit.toChronoUnit());
		} catch (Exception e) {
            throw new IllegalArgumentException("Unsupported unit " + timeUnit + " for value \"" + value + "\": " + e.getMessage());
		}
	}

	private static Duration toDuration(String value, TimeUnit timeUnit) {
		return isDurationString(value) ? Duration.parse(value) : toDuration(Long.parseLong(value), timeUnit);
	}

	private static boolean isDurationString(String value) {
		return value.length() > 1 && (isP(value.charAt(0)) || isP(value.charAt(1)));
	}

	private static boolean isP(char ch) {
		return ch == 'P' || ch == 'p';
	}
	// @formatter:on


	/**
	 * Scheduled 등록
	 */
	public void createScheduledTask(final String beanName, final String methodName, final String cronExpression, Object... args) {
		Object beanClass;
		try {
			if (beanName != null && beanName.contains(".")) {
				beanClass = applicationContext.getBean(ClassUtils.getClass(beanName));
			} else {
				beanClass = applicationContext.getBean(beanName);
			}
		} catch (Exception e) {
    		log.error("[등록된 Job 없음] 배치작업코드 = {}.{}", beanName, methodName);
    		return;
		}

		Method jobMethod;
		if (args != null && args.length > 0) {
			jobMethod = MethodUtils.getAccessibleMethod(beanClass.getClass(), methodName, ClassUtils.toClass(args));
		} else {
			jobMethod = MethodUtils.getAccessibleMethod(beanClass.getClass(), methodName);
		}
		if (jobMethod == null) {
			log.error("### 배치 스케쥴러 등록 실패 = {}.{}", beanName, methodName);
			return;
		}

		Scheduled scheduledMethod = new Scheduled() {
			public Class<? extends Annotation> annotationType() { return null; }
			public String cron() { return cronExpression; }
			public String zone() { return null; }
			public long fixedDelay() { return -1; }
			public String fixedDelayString() { return null; }
			public long fixedRate() { return -1; }
			public String fixedRateString() { return null; }
			public long initialDelay() { return -1; }
			public String initialDelayString() { return null; }
			public TimeUnit timeUnit() { return TimeUnit.MILLISECONDS; }
			public String scheduler() { return ""; }
		};

		processScheduledSync(scheduledMethod, jobMethod, beanClass, args);
	}

	/**
	 * Scheduled 삭제
	 */
	public void removeScheduledTask(final String beanName, final String methodName) {
		Object beanClass;
		try {
			if (beanName != null && beanName.contains(".")) {
				beanClass = applicationContext.getBean(ClassUtils.getClass(beanName));
			} else {
				beanClass = applicationContext.getBean(beanName);
			}
		} catch (Exception e) {
    		log.error("[등록된 Job 없음] 배치작업코드 = {}.{}", beanName, methodName);
    		return;
		}

		postProcessBeforeDestruction(beanClass, null);
	}

	/**
	 * Scheduled 목록 조회
	 */
	public List<ScheduledTask> getListScheduledTask() {
		return new ArrayList<>(getScheduledTasks());
	}

	/**
	 * ScheduledTask 실행 중인지 여부
	 */
	public boolean isExecuteTask(final String className, final String methodName) {
		return (getScheduledTasks().stream().anyMatch(x -> x.getTask().toString().equals(className + "." + methodName)));
	}

	/**
	 * 등록된 Cron Task 목록 조회
	 */
	public List<CronTask> getListCronTask(String className, String methodName, String cronExpression) {
		List<CronTask> list = new ArrayList<>();
		for (ScheduledTask task : getListScheduledTask()) {
			CronTask cronTask = (CronTask) task.getTask();
			ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) cronTask.getRunnable();
			log.debug("### ScheduledTask List = {}, {}, {}",
					runnable.getTarget().getClass().getSuperclass().getName(),
					runnable.getMethod().getName(),
					cronTask.getExpression());
		}
		return list;
	}

}
