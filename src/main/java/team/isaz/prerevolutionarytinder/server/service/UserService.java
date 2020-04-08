package team.isaz.prerevolutionarytinder.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import team.isaz.prerevolutionarytinder.server.domain.entities.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

public class UserService {

    @Autowired
    UserRepository userRepository;
    /*
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }*/

    public void insertSample() {
        User u = new User();
        u.setUsername("Name");
        u.setPassword("Password");
        u.setProfile_message("");
        u.setRole(0);
        userRepository.save(u);
        System.out.println(u);
    }
}
