package kr.fasol.obix.core.base;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service DTO에서 상속받는 공통 DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractServiceDto extends AbstractBaseDTO implements Serializable, Cloneable {
	@Serial
	private static final long serialVersionUID = -5358402582342767442L;


	public Object clone() {
        return super.clone();
    }
	public <T> T clone(Class<T> requiredType) {
		return super.clone(requiredType);
    }

	/**
	 * JSON 문자열로 반환
	 */
	public String toJSONString() {
		return new Gson().toJson(this);
	}

}
