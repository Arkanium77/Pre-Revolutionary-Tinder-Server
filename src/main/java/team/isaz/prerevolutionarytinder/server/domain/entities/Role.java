package team.isaz.prerevolutionarytinder.server.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    private int id;

    private String roleName;

}
