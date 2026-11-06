package kr.fasol.obix.core.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import kr.fasol.obix.core.util.XssUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * MyRequestWrapper 클래스는 HttpServletRequest를 래핑하여 XSS 공격을 방지하기 위한 기능을 제공합니다.
 * @author 정영탁
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {
    private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;

    public MyRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();
    }

    public void resetInputStream(byte[] data) {
        servletStream.stream = new ByteArrayInputStream(data);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(servletStream));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public byte[] getRawData() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return rawData;
    }

    private String cleanXSS(String value) {
        return XssUtil.escapeXss(value);
    }

    /**
     * ResettableServletInputStream 클래스는 ServletInputStream을 재설정할 수 있는 기능을 제공합니다.
     */
    private static class ResettableServletInputStream extends ServletInputStream {
        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }
        @Override
        public boolean isFinished() {
            return false;
        }
        @Override
        public boolean isReady() {
            return false;
        }
        @Override
        public void setReadListener(ReadListener listener) {
        }
    }

}
