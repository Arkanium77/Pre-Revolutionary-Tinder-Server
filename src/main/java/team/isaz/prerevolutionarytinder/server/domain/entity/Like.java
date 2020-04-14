package team.isaz.prerevolutionarytinder.server.domain.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "WHO")
    private UUID who;

    @Column(name = "WHOM")
    private UUID whom;

    @Column(name = "LIKED")
    private boolean liked;
}
