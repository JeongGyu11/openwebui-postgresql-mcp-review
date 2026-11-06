package kr.fasol.obix.core.config.properties;

import lombok.Data;

/**
 * 설명 : DBCP용 접속정보 설정
 *
 * @Class DbcpDBProperties.class
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
public class DbcpDBProperties {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private int maxActive;
    private int minIdle;
    private String scanPackage;
    private String poolName;
    private String jndiName;
    private boolean autoReconnect = true;
    private boolean cacheServerConfiguration = true;
    private boolean useLocalSessionState = true;
    private boolean elideSetAutoCommits = true;
    private int connectTimeout = 3000;
    private int socketTimeout = 60000;
    private boolean useSSL = false;
    private boolean useAffectedRows = true;
    private boolean useUnicode = true;
    private String characterEncoding = "UTF-8";
}
