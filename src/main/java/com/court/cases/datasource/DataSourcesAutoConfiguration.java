package com.court.cases.datasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;

/**
 * 数据源配置，新增一个数据源时，修改标注部分
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnBean({
        CasesDataSourceConfig.class
        //新增数据源改这里
        //XxxDataSourceConfig.class
})
public class DataSourcesAutoConfiguration implements TransactionManagementConfigurer {

    @Resource
    private CasesDataSourceConfig casesDataSourceConfig;
    //新增数据源改这里
    //@Resource
    //private XxxDataSourceConfig xxxDataSourceConfig;

    //配置分布式事务管理
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new ChainedTransactionManager(
                casesDataSourceConfig.casesTransactionManager()
                //新增数据源改这里
                //xxxDataSourceConfig.xxxTransactionManager()
        );
    }

}