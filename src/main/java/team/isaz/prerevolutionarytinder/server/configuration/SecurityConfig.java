package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfig.class)
public class SecurityConfig {
}
