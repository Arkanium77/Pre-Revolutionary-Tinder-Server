package team.isaz.prerevolutionarytinder.server.configuration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import team.isaz.prerevolutionarytinder.server.service.LoginService;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("team.isaz.prerevolutionarytinder.server.domain.repository")
public class AppJavaConfig {
    @Bean
    public LoginService userService() {
        return new LoginService();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("1501");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tinder");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        entityManagerFactoryBean.setJpaProperties(hibProperties());
        entityManagerFactoryBean.setPackagesToScan("team.isaz.prerevolutionarytinder.server.domain.entities");
        return entityManagerFactoryBean;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.put("hibernate.show_sql", true);
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

}
