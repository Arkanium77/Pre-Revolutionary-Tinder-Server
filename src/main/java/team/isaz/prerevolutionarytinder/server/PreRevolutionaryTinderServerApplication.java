package team.isaz.prerevolutionarytinder.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class PreRevolutionaryTinderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreRevolutionaryTinderServerApplication.class, args);
        //ApplicationContext context = new AnnotationConfigApplicationContext(AppJavaConfig.class);
        //var service = (UserService) context.getBean("userService");
    }

}
