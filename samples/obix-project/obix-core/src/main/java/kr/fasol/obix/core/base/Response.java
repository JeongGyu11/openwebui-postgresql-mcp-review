package kr.fasol.obix.core.base;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.fasol.obix.core.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JSON 응답 객체
 */
@Getter
@ToString
public class Response<T> {

    public static final Response<?> SUCCESS = Response.builder().status("CB0000", "성공하였습니다").build();
    public static final Response<?> ERROR = Response.builder().status("CB1000", "처리가 정상적으로 되지 않았습니다.").build();
    public static final Response<?> BAD_REQUEST = Response.builder().status("CB1003", "잘못된 요청입니다.").build();

    private String status;
    private String message;
    private Object common;
    //private ErrorInfo errorInfo;
    private T result;
    private String detailMessage;

    public Response() {
    }
    public Response(String status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
    public Response(String status, String message, Object common, T result) {
        this.status = status;
        this.message = message;
        this.common = common;
        this.result = result;
    }
    public Response(String status, String message, Object common, T result, ErrorInfo errorInfo, String detailMessage) {
        this.status = status;
        this.message = message;
        this.common = common;
        this.result = result;
        //this.errorInfo = errorInfo;
        this.detailMessage = detailMessage;
    }

    public static <T> Response<T> ok(T body) {
        return new ResponseBuilder<T>().result(body).build();
    }

    public static <T> ResponseBuilder<T> builder() {
        return new ResponseBuilder<>();
    }

    public static <T> Map<String, ?> convertMap(Response<T> response) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", response.getStatus());
        result.put("message", response.getMessage());
        result.put("common", response.getCommon());
        if (response.getResult() != null) {
            result.put("result", JsonUtil.convertValue(response.getResult(), new TypeReference<T>() {}));
        }
        result.put("detailMesssage", response.getDetailMessage());
        result.put("success", response.isSuccess());
        return result;
    }

    public boolean isSuccess() {
        return StringUtils.equals(status, "CB0000");
    }

    @Getter
    @Setter
    @Builder
    public static class ErrorInfo {
        private String errCd;
        private String errMsgCtnt;
        private String errMsgDesc;
        private String errMngmCtnt;
        private String errLoctCtnt;
    }

    public static class ResponseBuilder<T> {
        private String status = "CB0000";
        private String message = "성공하였습니다.";
        private Object common;
        private String detailMessage;
        private T result;
        private ErrorInfo errorInfo;

        ResponseBuilder() {
        }

        public Response.ResponseBuilder<T> status(String code, String message) {
            this.status = code;
            this.message = message;
            return this;
        }

        public Response.ResponseBuilder<T> statusAndMessage(String status, String message) {
            return status(status, message);
        }

        public Response.ResponseBuilder<T> common(Object common) {
            this.common = common;
            return this;
        }

        public Response.ResponseBuilder<T> errorInfo(ErrorInfo errorInfo) {
            this.errorInfo = errorInfo;
            return this;
        }

		public Response.ResponseBuilder<T> result(T result) {
            this.result = result;
            return this;
        }

        public Response.ResponseBuilder<T> detailMessage(String detailMessage) {
            this.detailMessage = detailMessage;
            return this;
        }

        public Response<T> build() {
            return new Response<>(status, message, common, result, errorInfo, detailMessage);
        }
    }
}
