package com.pronostiapp.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pronostiapp.com.domain.util.ScoreUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserPronostic.
 */
@Entity
@Table(name = "userpronostic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserPronostic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "score_team_1")
    private Long scoreTeam1;

    @Column(name = "score_team_2")
    private Long scoreTeam2;

    @Column(name = "pronostic_date")
    private ZonedDateTime pronosticDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Team winnerTeam;

    @ManyToOne
    private Match match;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoreTeam1() {
        return scoreTeam1;
    }

    public void setScoreTeam1(Long scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public Long getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(Long scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public ZonedDateTime getPronosticDate() {
        return pronosticDate;
    }

    public void setPronosticDate(ZonedDateTime pronosticDate) {
        this.pronosticDate = pronosticDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getWinnerTeam() {
        return winnerTeam;
    }

    public void setWinnerTeam(Team team) {
        this.winnerTeam = team;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPronostic userPronostic = (UserPronostic) o;
        if(userPronostic.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userPronostic.id);
    }

    public int getPoints(){
        if(match == null)
            return 0;
        ZonedDateTime now = ZonedDateTime.now();
        if(match.getMatchDate().isAfter(now)){
            return 0;
        }
        if(match.getScoreTeam1() == null ||
            match.getScoreTeam2() == null ||
            match.getWinner() == null){
            return 0;
        }
        int points = 0;
        if(match.getScoreTeam1() == scoreTeam1 &&
            match.getScoreTeam2() == scoreTeam2 &&
            match.getWinner() == winnerTeam){
            points = ScoreUtil.MAX_POINT;
        } else {
            long diffRealMatch = match.getScoreTeam1() - match.getScoreTeam2();
            long diffPronostic = scoreTeam1 - scoreTeam2;
            if((diffPronostic > 0 && diffRealMatch > 0) ||
                (diffPronostic < 0 && diffRealMatch < 0) ||
                (diffPronostic == diffRealMatch)){
                points = ScoreUtil.AVERAGE_POINT;
            }
        }

        return points;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserPronostic{" +
            "id=" + id +
            ", scoreTeam1='" + scoreTeam1 + "'" +
            ", scoreTeam2='" + scoreTeam2 + "'" +
            ", pronosticDate='" + pronosticDate + "'" +
            '}';
    }
}
