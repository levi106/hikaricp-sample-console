package com.example.hikaricpsampleconsole;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.maxlifetime:1800000}")
    private Integer maxLifetime;

    @Value("${spring.datasource.idletimeout:600000}")
    private Integer idleTimeout;

    @Value("${spring.datasource.connectiontimeout:30000}")
    private Integer connectionTimeout;

    @Value("${spring.datasource.maxpoolsize:10}")
    private Integer maxPoolSize;

    @Value("${spring.datasource.keepalivetime:0}")
    private Integer keepaliveTime;

    @Value("${spring.datasource.connectiontestquery:}")
    private String connectionTestQuery;

    @Bean
    public DataSource getDataSource() {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setMaxLifetime(maxLifetime);
        config.setIdleTimeout(idleTimeout);
        config.setConnectionTimeout(connectionTimeout);
        config.setMaximumPoolSize(maxPoolSize);
        config.setKeepaliveTime(keepaliveTime);
        if (connectionTestQuery.length() > 0) {
            config.setConnectionTestQuery(connectionTestQuery);
        }

        final HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
}
