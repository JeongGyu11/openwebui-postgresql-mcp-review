package kr.fasol.obix.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > Properties 파일 내용 조회
 * 프로그램명 : Properties 파일 내용 조회
 * 설명 : 
 *
 * @Class  EnvironmentUtil.class
 * @author  정영탁
 * @since   2017-09-08
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-09-08        정영탁    최초생성
 */
@Component
public class EnvironmentUtil {

    private static Environment environment;

    @Autowired
    public EnvironmentUtil(Environment env) {
        environment = env;
    }

    public static boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    public static String getRequiredProperty(String key) throws IllegalStateException {
        return environment.getRequiredProperty(key);
    }

    public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return environment.getRequiredProperty(key, targetType);
    }

    public static String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    public static String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return environment.resolveRequiredPlaceholders(text);
    }

}
