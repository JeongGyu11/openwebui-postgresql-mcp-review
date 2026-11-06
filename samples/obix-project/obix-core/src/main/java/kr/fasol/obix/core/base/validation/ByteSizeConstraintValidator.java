package kr.fasol.obix.core.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

@Slf4j
public class ByteSizeConstraintValidator implements ConstraintValidator<ByteSize, String> {

    private int min;
    private int max;
    private String charset;

    @Override
    public void initialize(ByteSize constraintAnnotation) {
        charset = constraintAnnotation.charset();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final String v = StringUtils.defaultString(value, StringUtils.EMPTY);
        try {
            final int len = v.getBytes(charset).length;
            return len >= min && len <= max;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
