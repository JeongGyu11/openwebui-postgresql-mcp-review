package kr.fasol.obix.core.config.db.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.MDC;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 프로그램명 : SQL 로그를 출력하기 위한 Interceptor
 * <pre>
 * - 호출 순서 :
 *  1. Executor의 proceed() 이전 수행
 *  2. StatementHandler의 proceed() 이전 수행
 *  3. StatementHandler의 proceed() 이후 수행
 *  4. Executor의 proceed() 이후 수행
 * </pre>
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        ,@Signature(type = Executor.class, method = "query" , args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        ,@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlLogInterceptor implements Interceptor {
    private static final Pattern PARAM_PLACEHOLDER = Pattern.compile("\\?");

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        if (invocation.getTarget() instanceof Executor) {
            return interceptExecutor(invocation);
        } else {
            return interceptStatementHandler(invocation);
        }
    }

    /**
     * Executor를 가로채서 SQL-ID 추출
     */
    private Object interceptExecutor(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        MDC.put("SQL-ID", mappedStatement.getId());
        MDC.put("SQL-COMMAND-TYPE", (mappedStatement.getSqlCommandType() != null ? mappedStatement.getSqlCommandType().name() : ""));

        // implement pre processing if need
        Object returnObject = invocation.proceed();
        // implement post processing if need

        int resultCount = getResultCount(returnObject);
        log.debug("SQL-결과건수 : {}", resultCount);

        return returnObject;
    }

    /**
     * SQL 로그 출력
     */
    public Object interceptStatementHandler(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        String sqlId = MDC.get("SQL-ID");
        String sqlCommandType = MDC.get("SQL-COMMAND-TYPE");
        MDC.remove("SQL-ID");
        MDC.remove("SQL-COMMAND-TYPE");

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");

        String originalSql = boundSql.getSql(); // 원본 SQL
        Object parameterObject = boundSql.getParameterObject(); // 파라미터

        // SQL에 파라미터 값 매핑
        String filledSql = replacePlaceholders(originalSql, parameterObject, boundSql);

        if (log.isDebugEnabled()) {
            log.debug("SQL-COMMAND-TYPE : {}", sqlCommandType);
            log.debug("SQL-ID : {}", sqlId);
            log.debug("Executing SQL: {}", filledSql);
        }

        // implement pre processing if need
        Object returnObject = invocation.proceed();
        // implement post processing if need

        return returnObject;
    }

    /**
     * SQL에 파라미터 값 매핑
     */
    private String replacePlaceholders(String sql, Object parameterObject, BoundSql boundSql) {
        List<Object> parameters = extractParameters(parameterObject, boundSql);

        Matcher matcher = PARAM_PLACEHOLDER.matcher(sql);
        StringBuilder sb = new StringBuilder();
        int index = 0;

        try {
            while (matcher.find() && index < parameters.size()) {
                Object value = parameters.get(index++);
                String replacement = (value instanceof String) ? "'" + value + "'" : value == null ? "NULL" : value.toString();
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            log.error("Failed to replace placeholders in SQL: {}", sql, e);
            return sql;
        }
    }

    /**
     * 파라미터 추출
     */
    private List<Object> extractParameters(Object parameterObject, BoundSql boundSql) {
        List<Object> parameters = new ArrayList<>();
        if (parameterObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
            paramMap.forEach((key, value) -> {
                if (!key.startsWith("__frch")) { // MyBatis 내부 키 필터링
                    parameters.add(value);
                }
            });
        } else if (parameterObject != null) {
            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            boundSql.getParameterMappings().stream()
                    .map(ParameterMapping::getProperty)
                    .filter(metaObject::hasGetter)
                    .forEach(name -> parameters.add(metaObject.getValue(name)));
        }
        return parameters;
    }

    /**
     * 쿼리 실행결과 건수 반환
     */
    private int getResultCount(Object returnObject) {
        if (returnObject instanceof List<?>) {
            return ((List<?>) returnObject).size();
        } else if (returnObject instanceof Object[]) {
            return ((Object[]) returnObject).length;
        } else if (returnObject instanceof Long) {
            return ((Long) returnObject).intValue();
        } else if (returnObject instanceof Integer) {
            return (Integer) returnObject;
        } else if (returnObject != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 필요시 설정값 사용
    }

}
