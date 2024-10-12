package de.supercode.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Player must have a name")
    private String name;

    private int powerlevel;

    private PlayerTypes playerType;

    public Player() {
    }

    public Player(String name, String type) {
        this.name = name;
        this.playerType = PlayerTypes.valueOf(type);
    }

    public Player(long id, String name, int powerlevel, PlayerTypes playerType) {
        this.id = id;
        this.name = name;
        this.powerlevel = powerlevel;
        this.playerType = playerType;
    }
}
