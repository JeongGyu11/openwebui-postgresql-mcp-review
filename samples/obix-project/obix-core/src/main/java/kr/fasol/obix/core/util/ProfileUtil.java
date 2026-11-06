package kr.fasol.obix.core.util;

import kr.fasol.obix.core.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Spring Boot의 Active Profile 조회
 */
@Component
public class ProfileUtil {

    private static Environment environment;

    @Autowired
    public ProfileUtil(Environment env) {
        environment = env;
    }

    public static String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }

    public static String getActiveProfile() {
        return Arrays.asList(environment.getActiveProfiles()).getFirst();
    }

    public static boolean acceptsProfiles(String... profiles) {
        return environment.acceptsProfiles(Profiles.of(profiles));
    }

    public static boolean acceptsProfiles(Profiles profiles) {
        return environment.acceptsProfiles(profiles);
    }

    public static boolean isLocal() {
        return environment.acceptsProfiles(Profiles.of(Constants.ENV_LOCAL));
    }
    public static boolean isDev() {
        return environment.acceptsProfiles(Profiles.of(Constants.ENV_DEV));
    }
    public static boolean isTest() {
        return environment.acceptsProfiles(Profiles.of(Constants.ENV_TEST));
    }
    public static boolean isProduction() {
        return environment.acceptsProfiles(Profiles.of(Constants.ENV_PRODUCTION));
    }
    public static boolean isDr() {
        return environment.acceptsProfiles(Profiles.of(Constants.ENV_DR));
    }

}
