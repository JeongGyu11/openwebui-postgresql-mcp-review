package kr.fasol.obix.core.message.exception;

import kr.fasol.obix.core.base.Response;
import lombok.Getter;

import java.io.Serial;

/**
 * Http Client 호출 서비스에서 Exception 발생 시 사용
 */
@Getter
public class HttpClientApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3043251596161206213L;

    private final transient Response<?> response;
    private final Integer status;

    public HttpClientApiException(Response<?> response, Integer status) {
        super(response != null ? response.getDetailMessage() : null);
        this.response = response;
        this.status = status;
    }
}
