package org.example.fm;

import org.example.fm.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findPlayersByIdIn(Collection<Integer> ids);

    List<Player> findPlayersByTeamId(Integer teamId);
}
