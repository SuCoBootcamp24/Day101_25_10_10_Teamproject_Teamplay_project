package de.supercode.backend.repositorys;

import de.supercode.backend.entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
