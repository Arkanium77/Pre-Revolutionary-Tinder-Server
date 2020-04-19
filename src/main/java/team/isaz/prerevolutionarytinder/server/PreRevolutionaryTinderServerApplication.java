package team.isaz.prerevolutionarytinder.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PreRevolutionaryTinderServerApplication {

    public static void main(String[] args) {
        try {
            System.setProperty("username", args[0]);
            System.setProperty("password", args[1]);
        } catch (Exception e) {
            System.out.println("Program need 2 arguments (username, password).");
            return;
        }
        SpringApplication.run(PreRevolutionaryTinderServerApplication.class, args);
    }

}
