package team.isaz.prerevolutionarytinder.server.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User who;

    @ManyToOne
    private User whom;
}
