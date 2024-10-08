package de.lewens_markisen.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class LocalDataBaseConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties("spring.localdb.datasource")
	public DataSourceProperties localDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties("spring.localdb.datasource.hikari")
	public DataSource dataSource(
			@Qualifier("localDataSourceProperties") DataSourceProperties localDataSourceProperties) {
		return localDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource,
			EntityManagerFactoryBuilder builder) {

		Properties props = new Properties();
		props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");

		//@formatter:off
		LocalContainerEntityManagerFactoryBean efb = builder.dataSource(dataSource)
				.packages("de.lewens_markisen.domain.localDb")
				.persistenceUnit("local")
				.build();
		//@formatter:on

		efb.setJpaProperties(props);

		return efb;
	}

	@Primary
	@Bean
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory.getObject());
	}

	@Bean
	@Primary
	public JdbcTemplate localJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
