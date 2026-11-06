package kr.fasol.obix.core.base;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.nio.channels.IllegalSelectorException;

/**
 * Controller DTO에서 상속받는 공통 DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractControllerDto implements Serializable, Cloneable {
	@Serial
	private static final long serialVersionUID = -3511688595132214637L;


	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalSelectorException();
		}
	}
	public <T> T clone(Class<T> requiredType) {
		try {
			return requiredType.cast(super.clone());
		} catch (CloneNotSupportedException e) {
			throw new IllegalSelectorException();
		}
	}

	/**
	 * JSON 문자열로 반환
	 */
	public String toJSONString() {
		return new Gson().toJson(this);
	}

}
