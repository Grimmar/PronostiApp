package com.pronostiapp.com.web.rest;

import com.pronostiapp.com.PronostiApp;
import com.pronostiapp.com.domain.Team;
import com.pronostiapp.com.repository.TeamRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pronostiapp.com.domain.enumeration.Group;

/**
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PronostiApp.class)
@WebAppConfiguration
@IntegrationTest
public class TeamResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Group DEFAULT_TEAM_GROUP = Group.A;
    private static final Group UPDATED_TEAM_GROUP = Group.B;

    private static final Long DEFAULT_TEAM_PTS = 1L;
    private static final Long UPDATED_TEAM_PTS = 2L;

    private static final Long DEFAULT_TEAM_J = 1L;
    private static final Long UPDATED_TEAM_J = 2L;

    private static final Long DEFAULT_TEAM_G = 1L;
    private static final Long UPDATED_TEAM_G = 2L;

    private static final Long DEFAULT_TEAM_N = 1L;
    private static final Long UPDATED_TEAM_N = 2L;

    private static final Long DEFAULT_TEAM_P = 1L;
    private static final Long UPDATED_TEAM_P = 2L;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTeamMockMvc;

    private Team team;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamResource teamResource = new TeamResource();
        ReflectionTestUtils.setField(teamResource, "teamRepository", teamRepository);
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        team = new Team();
        team.setName(DEFAULT_NAME);
        team.setCode(DEFAULT_CODE);
        team.setTeamGroup(DEFAULT_TEAM_GROUP);
        team.setTeamPts(DEFAULT_TEAM_PTS);
        team.setTeamJ(DEFAULT_TEAM_J);
        team.setTeamG(DEFAULT_TEAM_G);
        team.setTeamN(DEFAULT_TEAM_N);
        team.setTeamP(DEFAULT_TEAM_P);
    }

    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team

        restTeamMockMvc.perform(post("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team)))
                .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teams.get(teams.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeam.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTeam.getTeamGroup()).isEqualTo(DEFAULT_TEAM_GROUP);
        assertThat(testTeam.getTeamPts()).isEqualTo(DEFAULT_TEAM_PTS);
        assertThat(testTeam.getTeamJ()).isEqualTo(DEFAULT_TEAM_J);
        assertThat(testTeam.getTeamG()).isEqualTo(DEFAULT_TEAM_G);
        assertThat(testTeam.getTeamN()).isEqualTo(DEFAULT_TEAM_N);
        assertThat(testTeam.getTeamP()).isEqualTo(DEFAULT_TEAM_P);
    }

    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teams
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].teamGroup").value(hasItem(DEFAULT_TEAM_GROUP.toString())))
                .andExpect(jsonPath("$.[*].teamPts").value(hasItem(DEFAULT_TEAM_PTS.intValue())))
                .andExpect(jsonPath("$.[*].teamJ").value(hasItem(DEFAULT_TEAM_J.intValue())))
                .andExpect(jsonPath("$.[*].teamG").value(hasItem(DEFAULT_TEAM_G.intValue())))
                .andExpect(jsonPath("$.[*].teamN").value(hasItem(DEFAULT_TEAM_N.intValue())))
                .andExpect(jsonPath("$.[*].teamP").value(hasItem(DEFAULT_TEAM_P.intValue())));
    }

    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.teamGroup").value(DEFAULT_TEAM_GROUP.toString()))
            .andExpect(jsonPath("$.teamPts").value(DEFAULT_TEAM_PTS.intValue()))
            .andExpect(jsonPath("$.teamJ").value(DEFAULT_TEAM_J.intValue()))
            .andExpect(jsonPath("$.teamG").value(DEFAULT_TEAM_G.intValue()))
            .andExpect(jsonPath("$.teamN").value(DEFAULT_TEAM_N.intValue()))
            .andExpect(jsonPath("$.teamP").value(DEFAULT_TEAM_P.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = new Team();
        updatedTeam.setId(team.getId());
        updatedTeam.setName(UPDATED_NAME);
        updatedTeam.setCode(UPDATED_CODE);
        updatedTeam.setTeamGroup(UPDATED_TEAM_GROUP);
        updatedTeam.setTeamPts(UPDATED_TEAM_PTS);
        updatedTeam.setTeamJ(UPDATED_TEAM_J);
        updatedTeam.setTeamG(UPDATED_TEAM_G);
        updatedTeam.setTeamN(UPDATED_TEAM_N);
        updatedTeam.setTeamP(UPDATED_TEAM_P);

        restTeamMockMvc.perform(put("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeam)))
                .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teams.get(teams.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTeam.getTeamGroup()).isEqualTo(UPDATED_TEAM_GROUP);
        assertThat(testTeam.getTeamPts()).isEqualTo(UPDATED_TEAM_PTS);
        assertThat(testTeam.getTeamJ()).isEqualTo(UPDATED_TEAM_J);
        assertThat(testTeam.getTeamG()).isEqualTo(UPDATED_TEAM_G);
        assertThat(testTeam.getTeamN()).isEqualTo(UPDATED_TEAM_N);
        assertThat(testTeam.getTeamP()).isEqualTo(UPDATED_TEAM_P);
    }

    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Get the team
        restTeamMockMvc.perform(delete("/api/teams/{id}", team.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeDelete - 1);
    }
}
