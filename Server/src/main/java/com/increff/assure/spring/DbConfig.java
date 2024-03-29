package com.increff.assure.spring;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@EnableTransactionManagement
@Configuration
public class DbConfig {

    public static final String PACKAGE_POJO = "com.increff.assure.pojo";

    @Value("${jdbc.driverClassName}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddl;
    @Value("${hibernate.physical_naming_strategy}")
    private String hibernatePhysicalNamingStrategy;
    @Value("${hibernate.set_validation_query}")
    private String hibernateSetValidationQuery;
    @Value("${hibernate.set_min_idle}")
    private String hibernateSetMinIdle;
    @Value("${hibernate.set_default_auto_commit}")
    private String hibernateSetDefaultAutoCommit;
    @Value("${hibernate.set_test_while_idle}")
    private String hibernateSetTestWhileIdle;
    @Value("${hibernate.set_initial_size}")
    private String hibernateSetInitialSize;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        BasicDataSource bean = new BasicDataSource();
        bean.setDriverClassName(jdbcDriver);
        bean.setUrl(jdbcUrl);
        bean.setUsername(jdbcUsername);
        bean.setPassword(jdbcPassword);
        bean.setInitialSize(Integer.parseInt(hibernateSetInitialSize));
        bean.setDefaultAutoCommit(Boolean.parseBoolean(hibernateSetDefaultAutoCommit));
        //bean.setMaxTotal(10);
        bean.setMinIdle(Integer.parseInt(hibernateSetMinIdle));
        bean.setValidationQuery(hibernateSetValidationQuery);
        bean.setTestWhileIdle(Boolean.parseBoolean(hibernateSetTestWhileIdle));
        bean.setTimeBetweenEvictionRunsMillis(10 * 60 * 100);
        return bean;
    }

    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan(PACKAGE_POJO);
        HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(jpaAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
        jpaProperties.put("hibernate.physical_naming_strategy", hibernatePhysicalNamingStrategy);
        bean.setJpaProperties(jpaProperties);
        return bean;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager bean = new JpaTransactionManager();
        bean.setEntityManagerFactory(emf.getObject());
        return bean;
    }
}
