package kr.fasol.obix.core.config;

import kr.fasol.obix.core.config.properties.DatasourcePropertyHolder;
import kr.fasol.obix.core.config.properties.DbcpDBProperties;
import kr.fasol.obix.core.config.properties.HikariProperties;
import kr.fasol.obix.core.config.properties.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * 시스템명 :
 * 업무구분명 : F/W 설정 > DataSource 설정
 * 프로그램명 : DataSource 설정
 * 설명 :
 *
 * 클래스 : DataSourceConfiguration.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 20.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 20         정영탁          최초생성
 * --------------------------------------------------------------------
 */
@Configuration
public abstract class DataSourceConfiguration {

    @Autowired
    private Environment environment;

    public abstract String getDbName();

    /**
     * JNDI명 properties 설정
     */
    public DatasourcePropertyHolder datasourcePropertyHolder() {
        return getDatasourcePropertyHolder(getDbName());
    }
    public DbcpDBProperties dbcpDBProperties() {
        return getDbcpDBProperties(getDbName());
    }
    public MybatisProperties mybatisProperties() {
        return getMybatisProperties(getDbName());
    }
    public HikariProperties hikariProperties() {
        return new HikariProperties();
    }

    /**
     * Mybatis 접속정보 설정
     */
    private DatasourcePropertyHolder getDatasourcePropertyHolder(String jndiName) {
        Assert.notNull(jndiName, "`jndiName` is required");
        DatasourcePropertyHolder datasourcePropertyHolder = new DatasourcePropertyHolder();
        datasourcePropertyHolder.setJndiEnabled( environment.getProperty("database.config."+jndiName+".config.jndi.jndi-enabled", Boolean.class, false) );
        datasourcePropertyHolder.setJndiName( environment.getProperty("database.config."+jndiName+".config.jndi.jndi-name") );
        return datasourcePropertyHolder;
    }
    /**
     * DBCP용 접속정보 설정
     */
    private DbcpDBProperties getDbcpDBProperties(String jndiName) {
        Assert.notNull(jndiName, "`jndiName` is required");
        DbcpDBProperties dbcpDBProperties = new DbcpDBProperties();
        dbcpDBProperties.setUrl( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.url")) );
        dbcpDBProperties.setDriverClassName( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.driverClassName")) );
        dbcpDBProperties.setUsername( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.username")) );
        dbcpDBProperties.setPassword( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.password")) );
        dbcpDBProperties.setMaxActive( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.maxActive"), Integer.class) );
        dbcpDBProperties.setMinIdle( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.minIdle"), Integer.class) );
        dbcpDBProperties.setScanPackage( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.scanPackage")) );
        dbcpDBProperties.setPoolName( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.poolName")) );
        dbcpDBProperties.setJndiName( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.jndiName")) );
        dbcpDBProperties.setAutoReconnect( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.autoReconnect"), Boolean.class, true) );
        dbcpDBProperties.setCacheServerConfiguration( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.cacheServerConfiguration"), Boolean.class, true) );
        dbcpDBProperties.setUseLocalSessionState( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.useLocalSessionState"), Boolean.class, true) );
        dbcpDBProperties.setElideSetAutoCommits( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.elideSetAutoCommits"), Boolean.class, true) );
        dbcpDBProperties.setConnectTimeout( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.connectTimeout"), Integer.class, 3000) );
        dbcpDBProperties.setSocketTimeout( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.socketTimeout"), Integer.class, 60000) );
        dbcpDBProperties.setUseSSL( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.useSSL"), Boolean.class, false) );
        dbcpDBProperties.setUseAffectedRows( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.useAffectedRows"), Boolean.class, true) );
        dbcpDBProperties.setUseUnicode( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.useUnicode"), Boolean.class, true) );
        dbcpDBProperties.setCharacterEncoding( environment.getProperty("database.config.".concat(jndiName).concat(".datasource.characterEncoding"), "UTF-8") );
        return dbcpDBProperties;
    }
    /**
     * Mybatis 접속정보 설정
     */
    private MybatisProperties getMybatisProperties(String jndiName) {
        Assert.notNull(jndiName, "`jndiName` is required");
        MybatisProperties mybatisProperties = new MybatisProperties();
        mybatisProperties.setConfigLocation( environment.getProperty("database.config.".concat(jndiName).concat(".mybatis.configLocation")) );
        mybatisProperties.setMapperLocations( environment.getProperty("database.config.".concat(jndiName).concat(".mybatis.mapper-locations")) );
        mybatisProperties.setTypeAliasesPackage( environment.getProperty("database.config.".concat(jndiName).concat(".mybatis.type-aliases-package")) );
        mybatisProperties.setTypeHandlersPackage( environment.getProperty("database.config.".concat(jndiName).concat(".mybatis.type-handlers-package")) );
        return mybatisProperties;
    }

    /**
     * Mybatis Config의 properties 설정
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }

}
