package com.example.demo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 정책 관련 데이터베이스 설정을 담당하는 클래스
 */
@Configuration
@MapperScan(basePackages = "com.example.demo.repository.policy", sqlSessionFactoryRef = "policySqlSessionFactory")
public class PolicyDatabaseConfig {

    /**
     * Primary DataSource를 설정하는 메서드
     *
     * @return 정책 데이터베이스용 DataSource
     */
    @Primary
    @Bean(name = "policyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.policy")
    public DataSource dataSourceLog() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 정책 데이터베이스용 SqlSessionFactory를 설정하는 메서드
     *
     * @param dataSource 정책 데이터베이스용 DataSource
     * @return SqlSessionFactory
     * @throws Exception 설정 중 예외 발생 시
     */
    @Primary
    @Bean(name = "policySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("policyDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean
                .setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 정책 데이터베이스용 DataSourceTransactionManager를 설정하는 메서드
     *
     * @param dataSource 정책 데이터베이스용 DataSource
     * @return DataSourceTransactionManager
     */
    @Primary
    @Bean(name = "policyTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("policyDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
