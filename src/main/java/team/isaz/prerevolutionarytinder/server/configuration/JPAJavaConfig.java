package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAJavaConfig {
    @Value("${database.connection_string}")
    private String connectionString;

    @Bean
    public DataSource dataSource() {
        var source = new DriverManagerDataSource(
                connectionString,
                System.getProperty("username"),
                System.getProperty("password"));
        source.setDriverClassName("org.h2.Driver");
        return source;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("team.isaz.prerevolutionarytinder.server.domain.entity");
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaProperties(hibernateProperties());
        return factory;
    }

    Properties hibernateProperties() {
        Properties prop = new Properties();
        prop.setProperty("hibernate.hibernate.hbm2ddl.auto", "validate");//ВАЖНО
        prop.setProperty("hibernate.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        prop.setProperty("hibernate.show_sql", "true");
        return prop;
    }
}
