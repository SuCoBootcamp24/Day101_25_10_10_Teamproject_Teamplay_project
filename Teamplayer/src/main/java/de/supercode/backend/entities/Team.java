package de.supercode.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Team must have a name")
    private String name;

    private int wins;

    private int losses;

    @Column(nullable = false)
    private int ownerId;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Player> players = new HashSet<>();
}
