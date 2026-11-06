package kr.fasol.obix.core.config.properties;

import lombok.Data;

/**
 * 설명 : DB 접속정보 설정
 *
 * @Class MybatisProperties.class
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
public class MybatisProperties {
    private String configLocation;
    private String mapperLocations;
    private String typeAliasesPackage;
    private String typeHandlersPackage;
}
