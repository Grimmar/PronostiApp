package com.pronostiapp.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pronostiapp.com.domain.UserScore;
import com.pronostiapp.com.repository.UserScoreRepository;
import com.pronostiapp.com.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserScore.
 */
@RestController
@RequestMapping("/api")
public class UserScoreResource {

    private final Logger log = LoggerFactory.getLogger(UserScoreResource.class);
        
    @Inject
    private UserScoreRepository userScoreRepository;
    
    /**
     * POST  /user-scores : Create a new userScore.
     *
     * @param userScore the userScore to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userScore, or with status 400 (Bad Request) if the userScore has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-scores",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserScore> createUserScore(@RequestBody UserScore userScore) throws URISyntaxException {
        log.debug("REST request to save UserScore : {}", userScore);
        if (userScore.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userScore", "idexists", "A new userScore cannot already have an ID")).body(null);
        }
        UserScore result = userScoreRepository.save(userScore);
        return ResponseEntity.created(new URI("/api/user-scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userScore", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-scores : Updates an existing userScore.
     *
     * @param userScore the userScore to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userScore,
     * or with status 400 (Bad Request) if the userScore is not valid,
     * or with status 500 (Internal Server Error) if the userScore couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-scores",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserScore> updateUserScore(@RequestBody UserScore userScore) throws URISyntaxException {
        log.debug("REST request to update UserScore : {}", userScore);
        if (userScore.getId() == null) {
            return createUserScore(userScore);
        }
        UserScore result = userScoreRepository.save(userScore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userScore", userScore.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-scores : get all the userScores.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userScores in body
     */
    @RequestMapping(value = "/user-scores",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserScore> getAllUserScores() {
        log.debug("REST request to get all UserScores");
        List<UserScore> userScores = userScoreRepository.findAll();
        return userScores;
    }

    /**
     * GET  /user-scores/:id : get the "id" userScore.
     *
     * @param id the id of the userScore to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userScore, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-scores/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserScore> getUserScore(@PathVariable Long id) {
        log.debug("REST request to get UserScore : {}", id);
        UserScore userScore = userScoreRepository.findOne(id);
        return Optional.ofNullable(userScore)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-scores/:id : delete the "id" userScore.
     *
     * @param id the id of the userScore to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-scores/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserScore(@PathVariable Long id) {
        log.debug("REST request to delete UserScore : {}", id);
        userScoreRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userScore", id.toString())).build();
    }

}
