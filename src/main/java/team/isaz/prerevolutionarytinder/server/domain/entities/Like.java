package team.isaz.prerevolutionarytinder.server.domain.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @Id
    @GeneratedValue
    private Long id;

    private UUID who;
    private UUID whom;
}
