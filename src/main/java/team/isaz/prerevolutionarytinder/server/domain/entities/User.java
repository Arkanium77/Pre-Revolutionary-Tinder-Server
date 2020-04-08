package team.isaz.prerevolutionarytinder.server.domain.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    private String password;

    private int role;

    private boolean sex;

    private String profile_message;

}