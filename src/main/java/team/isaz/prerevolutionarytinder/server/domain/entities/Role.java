package team.isaz.prerevolutionarytinder.server.domain.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    private int id;

    private String roleName;

}
