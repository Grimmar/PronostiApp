package com.pronostiapp.com.repository;

import com.pronostiapp.com.domain.Match;

import com.pronostiapp.com.domain.User;
import com.pronostiapp.com.domain.enumeration.MatchType;
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

    List<Match> queryByMatchDateAfterOrderByMatchDateAsc(ZonedDateTime date);

    List<Match> queryByMatchDateBeforeOrderByMatchDateAsc(ZonedDateTime date);

    List<Match> queryByMatchTypeOrderByMatchDateAsc(MatchType matchType);

    List<Match> queryByOrderByMatchDateAsc();
}
