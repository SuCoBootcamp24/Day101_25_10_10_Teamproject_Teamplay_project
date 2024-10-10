package de.supercode.backend.repositorys;

import de.supercode.backend.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
