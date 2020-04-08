package team.isaz.prerevolutionarytinder.server.service;

import team.isaz.prerevolutionarytinder.server.domain.entities.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

import java.util.UUID;


public class Session {
    User user;
    UserRepository userRepository;

    Session(UUID id, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = this.userRepository.getOne(id);
    }
}
