package com.pronostiapp.com.web.rest;

import com.pronostiapp.com.PronostiApp;
import com.pronostiapp.com.domain.UserScore;
import com.pronostiapp.com.repository.UserScoreRepository;

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


/**
 * Test class for the UserScoreResource REST controller.
 *
 * @see UserScoreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PronostiApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserScoreResourceIntTest {


    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    @Inject
    private UserScoreRepository userScoreRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserScoreMockMvc;

    private UserScore userScore;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserScoreResource userScoreResource = new UserScoreResource();
        ReflectionTestUtils.setField(userScoreResource, "userScoreRepository", userScoreRepository);
        this.restUserScoreMockMvc = MockMvcBuilders.standaloneSetup(userScoreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userScore = new UserScore();
        userScore.setScore(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    public void createUserScore() throws Exception {
        int databaseSizeBeforeCreate = userScoreRepository.findAll().size();

        // Create the UserScore

        restUserScoreMockMvc.perform(post("/api/user-scores")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userScore)))
                .andExpect(status().isCreated());

        // Validate the UserScore in the database
        List<UserScore> userScores = userScoreRepository.findAll();
        assertThat(userScores).hasSize(databaseSizeBeforeCreate + 1);
        UserScore testUserScore = userScores.get(userScores.size() - 1);
        assertThat(testUserScore.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    public void getAllUserScores() throws Exception {
        // Initialize the database
        userScoreRepository.saveAndFlush(userScore);

        // Get all the userScores
        restUserScoreMockMvc.perform(get("/api/user-scores?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userScore.getId().intValue())))
                .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())));
    }

    @Test
    @Transactional
    public void getUserScore() throws Exception {
        // Initialize the database
        userScoreRepository.saveAndFlush(userScore);

        // Get the userScore
        restUserScoreMockMvc.perform(get("/api/user-scores/{id}", userScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userScore.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserScore() throws Exception {
        // Get the userScore
        restUserScoreMockMvc.perform(get("/api/user-scores/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserScore() throws Exception {
        // Initialize the database
        userScoreRepository.saveAndFlush(userScore);
        int databaseSizeBeforeUpdate = userScoreRepository.findAll().size();

        // Update the userScore
        UserScore updatedUserScore = new UserScore();
        updatedUserScore.setId(userScore.getId());
        updatedUserScore.setScore(UPDATED_SCORE);

        restUserScoreMockMvc.perform(put("/api/user-scores")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserScore)))
                .andExpect(status().isOk());

        // Validate the UserScore in the database
        List<UserScore> userScores = userScoreRepository.findAll();
        assertThat(userScores).hasSize(databaseSizeBeforeUpdate);
        UserScore testUserScore = userScores.get(userScores.size() - 1);
        assertThat(testUserScore.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void deleteUserScore() throws Exception {
        // Initialize the database
        userScoreRepository.saveAndFlush(userScore);
        int databaseSizeBeforeDelete = userScoreRepository.findAll().size();

        // Get the userScore
        restUserScoreMockMvc.perform(delete("/api/user-scores/{id}", userScore.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserScore> userScores = userScoreRepository.findAll();
        assertThat(userScores).hasSize(databaseSizeBeforeDelete - 1);
    }
}
