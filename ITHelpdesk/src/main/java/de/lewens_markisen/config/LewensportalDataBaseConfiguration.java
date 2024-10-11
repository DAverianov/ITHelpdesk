package de.lewens_markisen.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class LewensportalDataBaseConfiguration {

	@Bean
	@ConfigurationProperties("spring.lewensportal.datasource")
	public DataSourceProperties lewensportalDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource lewensportalDataSource(
			@Qualifier("lewensportalDataSourceProperties") DataSourceProperties lewensportalDataSourceProperties) {
		return lewensportalDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
    
    @Bean
    public JdbcTemplate lewensportalJdbcTemplate(@Qualifier("lewensportalDataSource") DataSource lewensportalDataSource) {
        return new JdbcTemplate(lewensportalDataSource);
    }

}
