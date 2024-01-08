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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 사용자 데이터베이스(MySQL)에 대한 설정을 담당하는 클래스.
 */
@Configuration
@MapperScan(basePackages = "com.example.demo.repository.user", sqlSessionFactoryRef = "userSqlSessionFactory")
public class UserDatabaseConfig {

    /**
     * 사용자 데이터베이스에 대한 DataSource 빈을 생성하는 메서드
     *
     * @return DataSource 빈
     */
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource dataSourceLog() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 사용자 데이터베이스에 대한 SqlSessionFactory 빈을 생성하는 메서드
     *
     * @param dataSource 사용할 DataSource
     * @return SqlSessionFactory 빈
     * @throws Exception SqlSessionFactory 생성 중 발생하는 예외
     */
    @Bean(name = "userSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean
                .setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 사용자 데이터베이스에 대한 트랜잭션 관리를 담당하는 TransactionManager 빈을 생성하는 메서드
     *
     * @param dataSource 사용할 DataSource
     * @return DataSourceTransactionManager 빈
     */
    @Bean(name = "userTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("userDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
