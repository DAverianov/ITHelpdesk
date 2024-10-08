package de.lewens_markisen.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FlywayConfiguration {

    @Bean
    @ConfigurationProperties("spring.localdb.flyway")
    public DataSourceProperties localDbFlywayDataSourceProps(){
        return new DataSourceProperties();
    }

    @Bean(initMethod = "migrate")
    public Flyway flywayLocalDB(@Qualifier("localDbFlywayDataSourceProps")
                             DataSourceProperties localDbFlywayDataSourceProps){
        return Flyway.configure()
                .dataSource(localDbFlywayDataSourceProps.getUrl(),
                		localDbFlywayDataSourceProps.getUsername(),
                		localDbFlywayDataSourceProps.getPassword())
                .locations("classpath:/db/migration")
                .load();
    }

}
