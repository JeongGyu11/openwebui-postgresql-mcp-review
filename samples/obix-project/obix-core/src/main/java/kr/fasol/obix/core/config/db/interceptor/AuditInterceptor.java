package kr.fasol.obix.core.config.db.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 프로그램명 : SQL Insert/Update 시의 Audit 컬럼 기본값 설정 Interceptor
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AuditInterceptor implements Interceptor {

    private final String applicationName;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String pageId = "";

        // request header에서 추출
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            pageId = request.getHeader("PAGE-ID");
            if (StringUtils.isBlank(pageId)) {
                pageId = applicationName;
            }
        } else {
            pageId = applicationName;
        }

        log.debug("### [AuditInterceptor] [pageId]: {}", pageId);

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (parameter instanceof AbstractBaseDTO dto) {
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                if (StringUtils.isBlank(dto.getFrstRegiUserId())) { dto.setFrstRegiUserId(pageId); }
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                if (StringUtils.isBlank(dto.getLastChngUserId())) { dto.setLastChngUserId(pageId); }
            }
        }
        return invocation.proceed();
    }

}
