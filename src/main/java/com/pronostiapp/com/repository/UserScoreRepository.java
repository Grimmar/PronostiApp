package com.pronostiapp.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pronostiapp.com.domain.User;
import com.pronostiapp.com.domain.UserScore;

/**
 * Spring Data JPA repository for the UserScore entity.
 */
@SuppressWarnings("unused")
public interface UserScoreRepository extends JpaRepository<UserScore,Long> {

    List<UserScore> queryFirst5ByOrderByScoreDesc();

}
