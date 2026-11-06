package kr.fasol.obix.core.config.properties;

import lombok.Data;

/**
 * 설명 : DataSource 정보 설정
 *
 * @Class DatasourcePropertyHolder.class
 * @author 정영탁
 * @since 2021/04/08
 * @version 1.0
 *
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021/04/08        정영탁          최초생성
 */
@Data
public class DatasourcePropertyHolder {
	private boolean jndiEnabled = false;
    private String jndiName;
}
