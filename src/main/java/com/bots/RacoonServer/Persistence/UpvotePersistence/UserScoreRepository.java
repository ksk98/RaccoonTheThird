package com.bots.RacoonServer.Persistence.UpvotePersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserScore u WHERE u.userId = :userId AND u.serverId = :serverId")
    boolean existsByUserAndServerId(@Param("userId") String userId, @Param("serverId") String serverId);

    @Query("SELECT u FROM UserScore u WHERE u.userId = :userId AND u.serverId = :serverId")
    UserScore getUserScoresForUserAndServerId(@Param("userId") String userId, @Param("serverId") String serverId);

    @Query("SELECT u FROM UserScore u WHERE u.userId = :userId")
    List<UserScore> getUserScoresForUser(@Param("userId") String userId);
}
