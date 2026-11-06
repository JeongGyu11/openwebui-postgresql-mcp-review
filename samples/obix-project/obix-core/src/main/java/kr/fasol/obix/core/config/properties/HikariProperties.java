package kr.fasol.obix.core.config.properties;

import lombok.Data;

/**
 * 설명 : DB 접속유지 테스트
 *
 * @Class   HikariProperties.java
 * @author  정영탁
 * @since   2017-07-19
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자              수정내용
 *--------------------------------------------------------------------
 * 2017-07-19        정영탁  최초생성
 */
@Data
public class HikariProperties {
    private String validationQuery;
}
