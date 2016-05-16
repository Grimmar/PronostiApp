package com.pronostiapp.com.domain;

import org.apache.commons.lang.WordUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import com.pronostiapp.com.domain.enumeration.MatchType;

/**
 * A Match.
 */
@Entity
@Table(name = "match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "match_date")
    private ZonedDateTime matchDate;

    @Column(name = "diffusion")
    private String diffusion;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type")
    private MatchType matchType;

    @ManyToOne
    private Team team_1;

    @ManyToOne
    private Team team_2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(ZonedDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public String getDiffusion() {
        return diffusion;
    }

    public void setDiffusion(String diffusion) {
        this.diffusion = diffusion;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Team getTeam_1() {
        return team_1;
    }

    public void setTeam_1(Team team) {
        this.team_1 = team;
    }

    public Team getTeam_2() {
        return team_2;
    }

    public void setTeam_2(Team team) {
        this.team_2 = team;
    }

    public String getDayDate(){
        return WordUtils.capitalize(matchDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE)) + " "
            + matchDate.getDayOfMonth()
            + " "
            + WordUtils.capitalize(matchDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE));
    }

    public String getHours(){
        return String.format("%02d", matchDate.getHour()) + ":"+ String.format("%02d",matchDate.getMinute());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Match match = (Match) o;
        if(match.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Match{" +
            "id=" + id +
            ", matchDate='" + matchDate + "'" +
            ", diffusion='" + diffusion + "'" +
            ", matchType='" + matchType + "'" +
            '}';
    }
}
