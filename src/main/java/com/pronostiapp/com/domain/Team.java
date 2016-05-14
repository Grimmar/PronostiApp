package com.pronostiapp.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pronostiapp.com.domain.enumeration.Group;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_group")
    private Group teamGroup;

    @Column(name = "team_pts")
    private Long teamPts;

    @Column(name = "team_j")
    private Long teamJ;

    @Column(name = "team_g")
    private Long teamG;

    @Column(name = "team_n")
    private Long teamN;

    @Column(name = "team_p")
    private Long teamP;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Group getTeamGroup() {
        return teamGroup;
    }

    public void setTeamGroup(Group teamGroup) {
        this.teamGroup = teamGroup;
    }

    public Long getTeamPts() {
        return teamPts;
    }

    public void setTeamPts(Long teamPts) {
        this.teamPts = teamPts;
    }

    public Long getTeamJ() {
        return teamJ;
    }

    public void setTeamJ(Long teamJ) {
        this.teamJ = teamJ;
    }

    public Long getTeamG() {
        return teamG;
    }

    public void setTeamG(Long teamG) {
        this.teamG = teamG;
    }

    public Long getTeamN() {
        return teamN;
    }

    public void setTeamN(Long teamN) {
        this.teamN = teamN;
    }

    public Long getTeamP() {
        return teamP;
    }

    public void setTeamP(Long teamP) {
        this.teamP = teamP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        if(team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", code='" + code + "'" +
            ", teamGroup='" + teamGroup + "'" +
            ", teamPts='" + teamPts + "'" +
            ", teamJ='" + teamJ + "'" +
            ", teamG='" + teamG + "'" +
            ", teamN='" + teamN + "'" +
            ", teamP='" + teamP + "'" +
            '}';
    }
}
