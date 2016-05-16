package com.pronostiapp.com.repository;

import com.pronostiapp.com.domain.Match;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Match entity.
 */
@SuppressWarnings("unused")
public interface MatchRepository extends JpaRepository<Match,Long> {

    List<Match> queryFirst5ByMatchDateBefore(ZonedDateTime date);

    List<Match> queryFirst5ByMatchDateAfterOrderByMatchDateAsc(ZonedDateTime date);
}
