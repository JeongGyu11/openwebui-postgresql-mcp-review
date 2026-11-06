package kr.fasol.obix.core.message.impl;

import com.google.gson.GsonBuilder;
import kong.unirest.*;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.message.exception.HttpClientApiException;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface HttpClientResponse<T> extends HttpResponse<T> {

    public static <T> HttpClientResponse<T> of(HttpResponse<T> httpResponse) {
        return new HttpClientResponse<T>() {
            @Override
            public int getStatus() {
                return httpResponse.getStatus();
            }

            @Override
            public String getStatusText() {
                return httpResponse.getStatusText();
            }

            @Override
            public Headers getHeaders() {
                return httpResponse.getHeaders();
            }

            @Override
            public T getBody() {
                return httpResponse.getBody();
            }

            @Override
            public Optional<UnirestParsingException> getParsingError() {
                return httpResponse.getParsingError();
            }

            @Override
            public <V> V mapBody(Function<T, V> function) {
                return httpResponse.mapBody(function);
            }

            @Override
            public <V> HttpResponse<V> map(Function<T, V> function) {
                return httpResponse.map(function);
            }

            @Override
            public HttpResponse<T> ifSuccess(Consumer<HttpResponse<T>> consumer) {
                return httpResponse.ifSuccess(consumer);
            }

            @Override
            public HttpResponse<T> ifFailure(Consumer<HttpResponse<T>> consumer) {
                return httpResponse.ifFailure(consumer);
            }

            @Override
            public <E> HttpResponse<T> ifFailure(Class<? extends E> aClass, Consumer<HttpResponse<E>> consumer) {
                return httpResponse.ifFailure(aClass, consumer);
            }

            @Override
            public boolean isSuccess() {
                return httpResponse.isSuccess();
            }

            @Override
            public <E> E mapError(Class<? extends E> aClass) {
                return httpResponse.mapError(aClass);
            }

            @Override
            public Cookies getCookies() {
                return httpResponse.getCookies();
            }

            @Override
            public HttpRequestSummary getRequestSummary() {
                return null;
            }
        };
    }

    default T get() {
        return this.getBody();
    }
    default T orElseThrowGet() throws HttpClientApiException {
        if (this.isSuccess()) {
            return this.getBody();
        }
        Response<?> errorResponse = this.getErrorResponse();
        throw new HttpClientApiException(errorResponse, this.getStatus());
    }
    default <X extends Throwable> T orElseThrowGet(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isSuccess()) {
            return this.getBody();
        }
        throw exceptionSupplier.get();
    }

    default Response<?> getErrorResponse() {
        if (!this.isSuccess()) {
            Optional<UnirestParsingException> parsingError = this.getParsingError();
            Response<?> errorResponse = null;
            if (parsingError.isPresent()) {
                final String body = parsingError.get().getOriginalBody();
                errorResponse = new GsonBuilder().create().fromJson(body, Response.class);
            }
            return errorResponse;
        }
        return Response.SUCCESS;
    }

}
