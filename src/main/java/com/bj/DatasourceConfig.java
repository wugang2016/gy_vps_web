package com.bj;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("bijie.datasource")
    public DataSourceProperties bijieDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("bijie.datasource")
    public DataSource bijieDataSource() {
        return bijieDataSourceProperties().initializeDataSourceBuilder().build();
    }
}