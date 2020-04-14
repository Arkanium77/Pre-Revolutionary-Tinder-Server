package team.isaz.prerevolutionarytinder.server.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "ROLE_NAME")
    private String roleName;


    public Role() {
        id = 0;
        roleName = "ROLE_USER";
    }

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
