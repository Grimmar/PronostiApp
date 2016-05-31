package com.pronostiapp.com.web.rest;

import com.pronostiapp.com.PronostiApp;
import com.pronostiapp.com.domain.UserPronostic;
import com.pronostiapp.com.repository.UserPronosticRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserPronosticResource REST controller.
 *
 * @see UserPronosticResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PronostiApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserPronosticResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_SCORE_TEAM_1 = 1L;
    private static final Long UPDATED_SCORE_TEAM_1 = 2L;

    private static final Long DEFAULT_SCORE_TEAM_2 = 1L;
    private static final Long UPDATED_SCORE_TEAM_2 = 2L;

    private static final ZonedDateTime DEFAULT_PRONOSTIC_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PRONOSTIC_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PRONOSTIC_DATE_STR = dateTimeFormatter.format(DEFAULT_PRONOSTIC_DATE);

    @Inject
    private UserPronosticRepository userPronosticRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserPronosticMockMvc;

    private UserPronostic userPronostic;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPronosticResource userPronosticResource = new UserPronosticResource();
        ReflectionTestUtils.setField(userPronosticResource, "userPronosticRepository", userPronosticRepository);
        this.restUserPronosticMockMvc = MockMvcBuilders.standaloneSetup(userPronosticResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userPronostic = new UserPronostic();
        userPronostic.setScoreTeam1(DEFAULT_SCORE_TEAM_1);
        userPronostic.setScoreTeam2(DEFAULT_SCORE_TEAM_2);
        userPronostic.setPronosticDate(DEFAULT_PRONOSTIC_DATE);
    }

    @Test
    @Transactional
    public void createUserPronostic() throws Exception {
        int databaseSizeBeforeCreate = userPronosticRepository.findAll().size();

        // Create the UserPronostic

        restUserPronosticMockMvc.perform(post("/api/user-pronostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPronostic)))
                .andExpect(status().isCreated());

        // Validate the UserPronostic in the database
        List<UserPronostic> userPronostics = userPronosticRepository.findAll();
        assertThat(userPronostics).hasSize(databaseSizeBeforeCreate + 1);
        UserPronostic testUserPronostic = userPronostics.get(userPronostics.size() - 1);
        assertThat(testUserPronostic.getScoreTeam1()).isEqualTo(DEFAULT_SCORE_TEAM_1);
        assertThat(testUserPronostic.getScoreTeam2()).isEqualTo(DEFAULT_SCORE_TEAM_2);
        assertThat(testUserPronostic.getPronosticDate()).isEqualTo(DEFAULT_PRONOSTIC_DATE);
    }

    @Test
    @Transactional
    public void getAllUserPronostics() throws Exception {
        // Initialize the database
        userPronosticRepository.saveAndFlush(userPronostic);

        // Get all the userPronostics
        restUserPronosticMockMvc.perform(get("/api/user-pronostics?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userPronostic.getId().intValue())))
                .andExpect(jsonPath("$.[*].scoreTeam1").value(hasItem(DEFAULT_SCORE_TEAM_1.intValue())))
                .andExpect(jsonPath("$.[*].scoreTeam2").value(hasItem(DEFAULT_SCORE_TEAM_2.intValue())))
                .andExpect(jsonPath("$.[*].pronosticDate").value(hasItem(DEFAULT_PRONOSTIC_DATE_STR)));
    }

    @Test
    @Transactional
    public void getUserPronostic() throws Exception {
        // Initialize the database
        userPronosticRepository.saveAndFlush(userPronostic);

        // Get the userPronostic
        restUserPronosticMockMvc.perform(get("/api/user-pronostics/{id}", userPronostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userPronostic.getId().intValue()))
            .andExpect(jsonPath("$.scoreTeam1").value(DEFAULT_SCORE_TEAM_1.intValue()))
            .andExpect(jsonPath("$.scoreTeam2").value(DEFAULT_SCORE_TEAM_2.intValue()))
            .andExpect(jsonPath("$.pronosticDate").value(DEFAULT_PRONOSTIC_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUserPronostic() throws Exception {
        // Get the userPronostic
        restUserPronosticMockMvc.perform(get("/api/user-pronostics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPronostic() throws Exception {
        // Initialize the database
        userPronosticRepository.saveAndFlush(userPronostic);
        int databaseSizeBeforeUpdate = userPronosticRepository.findAll().size();

        // Update the userPronostic
        UserPronostic updatedUserPronostic = new UserPronostic();
        updatedUserPronostic.setId(userPronostic.getId());
        updatedUserPronostic.setScoreTeam1(UPDATED_SCORE_TEAM_1);
        updatedUserPronostic.setScoreTeam2(UPDATED_SCORE_TEAM_2);
        updatedUserPronostic.setPronosticDate(UPDATED_PRONOSTIC_DATE);

        restUserPronosticMockMvc.perform(put("/api/user-pronostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserPronostic)))
                .andExpect(status().isOk());

        // Validate the UserPronostic in the database
        List<UserPronostic> userPronostics = userPronosticRepository.findAll();
        assertThat(userPronostics).hasSize(databaseSizeBeforeUpdate);
        UserPronostic testUserPronostic = userPronostics.get(userPronostics.size() - 1);
        assertThat(testUserPronostic.getScoreTeam1()).isEqualTo(UPDATED_SCORE_TEAM_1);
        assertThat(testUserPronostic.getScoreTeam2()).isEqualTo(UPDATED_SCORE_TEAM_2);
        assertThat(testUserPronostic.getPronosticDate()).isEqualTo(UPDATED_PRONOSTIC_DATE);
    }

    @Test
    @Transactional
    public void deleteUserPronostic() throws Exception {
        // Initialize the database
        userPronosticRepository.saveAndFlush(userPronostic);
        int databaseSizeBeforeDelete = userPronosticRepository.findAll().size();

        // Get the userPronostic
        restUserPronosticMockMvc.perform(delete("/api/user-pronostics/{id}", userPronostic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPronostic> userPronostics = userPronosticRepository.findAll();
        assertThat(userPronostics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
