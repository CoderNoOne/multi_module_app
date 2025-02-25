package org.example.bet;

import org.example.bet.entity.ScoreEntity;
import org.example.bet.enums.BetLeague;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreEntityRepository extends JpaRepository <ScoreEntity, Integer> {

    @Query(value = "select s from ScoreEntity  as s where s.user.username = :username")
    List<ScoreEntity> findAllByUserName(@Param("username") String username);

    @Query(value = "select s from ScoreEntity  as s where s.user.username = :username and s.league = :league")
    List<ScoreEntity> findAllByUserNameAndLeague(@Param("username") String username, @Param("league") BetLeague league);

    Optional<ScoreEntity> findByLeagueAndUserUsername(BetLeague league, String username);

    List<ScoreEntity> findAllByLeague(BetLeague league);


}
