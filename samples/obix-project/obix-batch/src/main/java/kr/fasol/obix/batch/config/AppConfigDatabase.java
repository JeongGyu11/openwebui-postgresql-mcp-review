package kr.fasol.obix.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import kr.fasol.obix.core.config.DataSourceConfiguration;
import kr.fasol.obix.core.config.db.interceptor.AuditInterceptor;
import kr.fasol.obix.core.config.db.interceptor.SqlLogInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 시스템명 : 
 * 업무구분명 : 설정 > Database 설정
 * 프로그램명 : Database 설정
 * 설명 :
 *
 * 클래스 : AppConfigDatabase.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 12. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 12. 13.         정영탁          최초생성
 */
@Configuration
@Lazy
@MapperScan(basePackages = "kr.fasol.**.dao", annotationClass = Mapper.class)
@EnableTransactionManagement
@EnableConfigurationProperties
public class AppConfigDatabase extends DataSourceConfiguration {

	@Autowired
	private Environment environment;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Override
	public String getDbName() {
		return environment.getProperty("database.default-config-name");
	}

    /**
     * 데이터소스 설정
     */
    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
    	if (datasourcePropertyHolder().isJndiEnabled()) {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            return dataSourceLookup.getDataSource(datasourcePropertyHolder().getJndiName());
    	} else {
            final HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(dbcpDBProperties().getUrl());
            dataSource.setDriverClassName(dbcpDBProperties().getDriverClassName());
            dataSource.setUsername(dbcpDBProperties().getUsername());
            dataSource.setPassword(dbcpDBProperties().getPassword());
            dataSource.setConnectionTestQuery(hikariProperties().getValidationQuery());
            dataSource.setMaximumPoolSize(dbcpDBProperties().getMaxActive());
            dataSource.setMinimumIdle(dbcpDBProperties().getMinIdle());
            dataSource.setPoolName(dbcpDBProperties().getPoolName());
            dataSource.addDataSourceProperty("autoReconnect", dbcpDBProperties().isAutoReconnect());
            dataSource.addDataSourceProperty("cacheServerConfiguration", dbcpDBProperties().isCacheServerConfiguration());
            dataSource.addDataSourceProperty("useLocalSessionState", dbcpDBProperties().isUseLocalSessionState());
            dataSource.addDataSourceProperty("elideSetAutoCommits", dbcpDBProperties().isElideSetAutoCommits());
            dataSource.addDataSourceProperty("connectTimeout", dbcpDBProperties().getConnectTimeout());
            dataSource.addDataSourceProperty("socketTimeout", dbcpDBProperties().getSocketTimeout());
            dataSource.addDataSourceProperty("useSSL", dbcpDBProperties().isUseSSL());
            dataSource.addDataSourceProperty("useAffectedRows", dbcpDBProperties().isUseAffectedRows());
            dataSource.addDataSourceProperty("useUnicode", dbcpDBProperties().isUseUnicode());
            dataSource.addDataSourceProperty("characterEncoding", dbcpDBProperties().getCharacterEncoding());
            return dataSource;
    	}
    }

    /**
     * SqlSessionFactory 설정
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        if (StringUtils.isNotBlank(mybatisProperties().getConfigLocation())) {
        	sqlSessionFactoryBean.setConfigLocation (resolver.getResource(mybatisProperties().getConfigLocation()));
        }
        if (StringUtils.isNotBlank(mybatisProperties().getMapperLocations())) {
        	sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mybatisProperties().getMapperLocations()));
        	sqlSessionFactoryBean.setConfiguration(configuration());
        }
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties().getTypeAliasesPackage());
        sqlSessionFactoryBean.setTypeHandlersPackage(mybatisProperties().getTypeHandlersPackage());

        // Mybatis Plugin 추가(Audit, SqlLog 등)
        sqlSessionFactoryBean.addPlugins(new SqlLogInterceptor());
        sqlSessionFactoryBean.addPlugins(new AuditInterceptor(applicationName));

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * SqlSessionTemplate 설정
     */
    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

}
