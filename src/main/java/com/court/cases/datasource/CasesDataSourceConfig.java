package com.court.cases.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 数据源配置，新增一个数据源时，复制该类，修改标注部分
 */
@Slf4j
@Configuration
//新增数据源改这里
@MapperScan(basePackages = CasesDataSourceConfig.MASTER_PACKAGE, sqlSessionFactoryRef = "casesSqlSessionFactory")
public class CasesDataSourceConfig {

    //新增数据源改这里
    static final String MASTER_PACKAGE = "com.court.cases.mybatis.mapper.cases";

    /**
     * 配置一个主库
     *
     * @return DruidDataSource
     */
    //新增数据源改这里
    @Bean(name = "casesDataSource")
    @Primary
    //新增数据源改这里
    @ConfigurationProperties(prefix = "cases.datasource")
    public DruidDataSource casesDruidDataSource() {
        return new DruidDataSource();
    }

    //新增数据源改这里
    @Bean(name = "casesSqlSessionFactory")
    @Primary
    public SqlSessionFactory casesSqlSessionFactory() {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(casesDruidDataSource());
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            //新增数据源改这里
            sessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper_diy/CasesMapper.xml"));
            return sessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error("配置cases的SqlSessionFactory失败，error:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //primary只能有一个
    @Primary
    //新增数据源改这里
    @Bean(name = "casesTransactionManager")
    public DataSourceTransactionManager casesTransactionManager() {
        return new DataSourceTransactionManager(casesDruidDataSource());
    }

}