package com.pronostiapp.com.repository;

import com.pronostiapp.com.domain.UserScore;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserScore entity.
 */
@SuppressWarnings("unused")
public interface UserScoreRepository extends JpaRepository<UserScore,Long> {

    @Query("select userScore from UserScore userScore where userScore.user.login = ?#{principal.username}")
    List<UserScore> findByUserIsCurrentUser();

}
