package kr.fasol.obix.core.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.nio.channels.IllegalSelectorException;
import java.util.Date;

/**
 * 설명 : VO에서 상속받는 공통 VO
 *
 * 클래스  AbstractBaseDTO.class
 * 작성자  정영탁
 * 작성일   2017-07-17
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-17           정영탁            최초생성
 */
@Getter
@Setter
public abstract class AbstractBaseDTO extends AbstractPageInfoDTO implements Serializable, Cloneable {
	@Serial
	private static final long serialVersionUID = 257153011932082877L;

    private String frstRegiUserId;	// 최초등록사용자아이디
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date frstRegiTmsp;	// 최초등록타임스탬프
    private String lastChngUserId;	// 최종변경사용자아이디
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastChngTmsp;	// 최종변경타임스탬프

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
