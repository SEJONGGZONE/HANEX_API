package com.gzonesoft.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;


/**
 * 
 * @author ships
 *
 */
@Configuration("dataSourceConfig")
@PropertySource(ignoreResourceNotFound=true,value="file:config/datasource.properties")
public class DataSourceConfig {
	@Value("${spring.db1.datasource.driverClassName}") private String driverClassName;
	@Value("${spring.db1.datasource.url}") private String url;
	@Value("${spring.db1.datasource.username}") private String username;
	@Value("${spring.db1.datasource.password}") private String password;

	@Value("${spring.db1.datasource.initialSize}") private String initialSize;
	@Value("${spring.db1.datasource.maxTotal}") private String maxTotal;
	@Value("${spring.db1.datasource.maxWaitMillis}") private String maxWaitMillis;
	
    @Bean(name = "db1DataSource")
    //@Primary
    @ConfigurationProperties(prefix = "spring.db1.datasource")
    public DataSource db1DataSource() {
//        return DataSourceBuilder.create().build();
    	
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        Properties dataSourceProp = new Properties();
        dataSourceProp.put("validationQuery", "select 1");
        dataSourceProp.put("timeBetweenEvictionRunsMillis", "60000");
        dataSourceProp.put("testWhileIdle", "true");
        dataSourceProp.put("initialSize", initialSize);
        dataSourceProp.put("maxTotal", maxTotal);
        dataSourceProp.put("maxWaitMillis", maxWaitMillis); //pool이 고갈되었을 경우 최대 대기시간
        
        dataSource.setConnectionProperties(dataSourceProp);
        return dataSource;    	
    }

    @Bean(name = "db1DataSourceFormatter")
    //@Primary
    public Log4jdbcProxyDataSource db1DataSourceFormatter(DataSource db1DataSource) throws Exception {
 
        return new Log4jdbcProxyDataSource(db1DataSource);
    }
    
}
