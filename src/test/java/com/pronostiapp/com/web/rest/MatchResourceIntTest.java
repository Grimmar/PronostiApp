package com.pronostiapp.com.web.rest;

import com.pronostiapp.com.PronostiApp;
import com.pronostiapp.com.domain.Match;
import com.pronostiapp.com.repository.MatchRepository;

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

import com.pronostiapp.com.domain.enumeration.MatchType;

/**
 * Test class for the MatchResource REST controller.
 *
 * @see MatchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PronostiApp.class)
@WebAppConfiguration
@IntegrationTest
public class MatchResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_MATCH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MATCH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MATCH_DATE_STR = dateTimeFormatter.format(DEFAULT_MATCH_DATE);
    private static final String DEFAULT_DIFFUSION = "AAAAA";
    private static final String UPDATED_DIFFUSION = "BBBBB";

    private static final MatchType DEFAULT_MATCH_TYPE = MatchType.POULE;
    private static final MatchType UPDATED_MATCH_TYPE = MatchType.HUITIEME;

    @Inject
    private MatchRepository matchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMatchMockMvc;

    private Match match;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MatchResource matchResource = new MatchResource();
        ReflectionTestUtils.setField(matchResource, "matchRepository", matchRepository);
        this.restMatchMockMvc = MockMvcBuilders.standaloneSetup(matchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        match = new Match();
        match.setMatchDate(DEFAULT_MATCH_DATE);
        match.setDiffusion(DEFAULT_DIFFUSION);
        match.setMatchType(DEFAULT_MATCH_TYPE);
    }

    @Test
    @Transactional
    public void createMatch() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();

        // Create the Match

        restMatchMockMvc.perform(post("/api/matches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(match)))
                .andExpect(status().isCreated());

        // Validate the Match in the database
        List<Match> matches = matchRepository.findAll();
        assertThat(matches).hasSize(databaseSizeBeforeCreate + 1);
        Match testMatch = matches.get(matches.size() - 1);
        assertThat(testMatch.getMatchDate()).isEqualTo(DEFAULT_MATCH_DATE);
        assertThat(testMatch.getDiffusion()).isEqualTo(DEFAULT_DIFFUSION);
        assertThat(testMatch.getMatchType()).isEqualTo(DEFAULT_MATCH_TYPE);
    }

    @Test
    @Transactional
    public void getAllMatches() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matches
        restMatchMockMvc.perform(get("/api/matches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(match.getId().intValue())))
                .andExpect(jsonPath("$.[*].matchDate").value(hasItem(DEFAULT_MATCH_DATE_STR)))
                .andExpect(jsonPath("$.[*].diffusion").value(hasItem(DEFAULT_DIFFUSION.toString())))
                .andExpect(jsonPath("$.[*].matchType").value(hasItem(DEFAULT_MATCH_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", match.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(match.getId().intValue()))
            .andExpect(jsonPath("$.matchDate").value(DEFAULT_MATCH_DATE_STR))
            .andExpect(jsonPath("$.diffusion").value(DEFAULT_DIFFUSION.toString()))
            .andExpect(jsonPath("$.matchType").value(DEFAULT_MATCH_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMatch() throws Exception {
        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match
        Match updatedMatch = new Match();
        updatedMatch.setId(match.getId());
        updatedMatch.setMatchDate(UPDATED_MATCH_DATE);
        updatedMatch.setDiffusion(UPDATED_DIFFUSION);
        updatedMatch.setMatchType(UPDATED_MATCH_TYPE);

        restMatchMockMvc.perform(put("/api/matches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMatch)))
                .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matches = matchRepository.findAll();
        assertThat(matches).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matches.get(matches.size() - 1);
        assertThat(testMatch.getMatchDate()).isEqualTo(UPDATED_MATCH_DATE);
        assertThat(testMatch.getDiffusion()).isEqualTo(UPDATED_DIFFUSION);
        assertThat(testMatch.getMatchType()).isEqualTo(UPDATED_MATCH_TYPE);
    }

    @Test
    @Transactional
    public void deleteMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);
        int databaseSizeBeforeDelete = matchRepository.findAll().size();

        // Get the match
        restMatchMockMvc.perform(delete("/api/matches/{id}", match.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Match> matches = matchRepository.findAll();
        assertThat(matches).hasSize(databaseSizeBeforeDelete - 1);
    }
}
