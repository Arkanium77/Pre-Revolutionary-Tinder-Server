package team.isaz.prerevolutionarytinder.server.domain.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PWD")
    private String password;

    @Column(name = "ROLES")
    private int roles;

    @Column(name = "SEX")
    private boolean sex;

    @Column(name = "PROFILE_MESSAGE")
    private String profileMessage;

}

