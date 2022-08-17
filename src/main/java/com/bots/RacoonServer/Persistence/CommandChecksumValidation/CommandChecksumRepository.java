package com.bots.RacoonServer.Persistence.CommandChecksumValidation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandChecksumRepository extends JpaRepository<CommandChecksum, Long> {

    @Query("SELECT c FROM CommandChecksum c WHERE c.commandKeyword = :keyword")
    CommandChecksum getChecksumForKeyword(@Param("keyword") String keyword);

    @Query("SELECT CASE WHEN COUNT(c)> 0 THEN TRUE ELSE FALSE END " +
            "FROM CommandChecksum c WHERE LOWER(c.commandKeyword) LIKE LOWER(:keyword)")
    boolean existsByKeyword(@Param("keyword") String keyword);
}
