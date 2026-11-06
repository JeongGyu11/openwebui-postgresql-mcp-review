package kr.fasol.obix.core.message.config;

/**
 * Http Client 호출 서비스에서 사용하는 상수 정의
 */
public class HttpClientConstant {

    public static enum MethodType {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH
    }

    public static enum Msa {
        BACKEND,
        ADMIN,
        BATCH,
        FRONT
    }

}
