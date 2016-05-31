package com.pronostiapp.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pronostiapp.com.domain.Match;
import com.pronostiapp.com.domain.User;
import com.pronostiapp.com.domain.UserPronostic;
import com.pronostiapp.com.domain.enumeration.MatchType;
import com.pronostiapp.com.repository.MatchRepository;
import com.pronostiapp.com.repository.UserPronosticRepository;
import com.pronostiapp.com.repository.UserRepository;
import com.pronostiapp.com.security.SecurityUtils;
import com.pronostiapp.com.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Match.
 */
@RestController
@RequestMapping("/api")
public class MatchResource {

    private final Logger log = LoggerFactory.getLogger(MatchResource.class);

    @Inject
    private MatchRepository matchRepository;

    @Inject
    private UserPronosticRepository userPronosticRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /matches : Create a new match.
     *
     * @param match the match to create
     * @return the ResponseEntity with status 201 (Created) and with body the new match, or with status 400 (Bad Request) if the match has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Match> createMatch(@RequestBody Match match) throws URISyntaxException {
        log.debug("REST request to save Match : {}", match);
        if (match.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("match", "idexists", "A new match cannot already have an ID")).body(null);
        }
        Match result = matchRepository.save(match);
        return ResponseEntity.created(new URI("/api/matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("match", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /matches : Updates an existing match.
     *
     * @param match the match to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated match,
     * or with status 400 (Bad Request) if the match is not valid,
     * or with status 500 (Internal Server Error) if the match couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Match> updateMatch(@RequestBody Match match) throws URISyntaxException {
        log.debug("REST request to update Match : {}", match);
        if (match.getId() == null) {
            return createMatch(match);
        }

        //if()

        Match result = matchRepository.save(match);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("match", match.getId().toString()))
            .body(result);
    }

    /**
     * GET  /matches : get all the matches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matches in body
     */
    @RequestMapping(value = "/matches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getAllMatches() {
        log.debug("REST request to get all Matches");
        List<Match> matches = matchRepository.findAll();
        return matches;
    }

    @RequestMapping(value = "/matchesOrdered",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getAllMatchesOrderedByMatchDate() {
        log.debug("REST request to get all Matches");
        List<Match> matches = matchRepository.queryByOrderByMatchDateAsc();
        return matches;
    }


    /**
     * GET  /matches : get all the matches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matches in body
     */
    @RequestMapping(value = "/nextMatches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getNextMatches() {
        log.debug("REST request to get 5 next Matches");
        ZonedDateTime today = ZonedDateTime.now();
        List<Match> matches = matchRepository.queryFirst5ByMatchDateAfterOrderByMatchDateAsc(today);
        return matches;
    }

    /**
     * GET  /matches : get all next matches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matches in body
     */
    @RequestMapping(value = "/allNextMatches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getAllNextMatches() {
        log.debug("REST request to get all next Matches");
        ZonedDateTime today = ZonedDateTime.now();
        String userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin);
        List<Match> matches = matchRepository.queryByMatchDateAfterOrderByMatchDateAsc(today);
        List<UserPronostic> pronosticsForCurrentUser = userPronosticRepository.findByUserIsCurrentUser();

        for(Match match : matches){
            Optional<UserPronostic> p = pronosticsForCurrentUser
                .stream()
                .filter(x -> x.getMatch().getId() == match.getId())
                .findFirst();
            if(p.isPresent()) {
                match.setUserPronosticForCurrentUser(p.get());
            } else {
                UserPronostic prono = new UserPronostic();
                try {
                    prono.setMatch(match.clone());
                }catch(CloneNotSupportedException ex){
                    //nothing
                }
                prono.setUser(currentUser.get());
                match.setUserPronosticForCurrentUser(prono);
            }
        }
        return matches;
    }

    @RequestMapping(value = "/allLastMatches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getAllLastMatches() {
        log.debug("REST request to get all next Matches");
        ZonedDateTime today = ZonedDateTime.now();
        String userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin);
        List<Match> matches = matchRepository.queryByMatchDateBeforeOrderByMatchDateAsc(today);
        List<UserPronostic> pronosticsForCurrentUser = userPronosticRepository.findByUserIsCurrentUser();

        for(Match match : matches){
            Optional<UserPronostic> p = pronosticsForCurrentUser
                .stream()
                .filter(x -> x.getMatch().getId() == match.getId())
                .findFirst();
            if(p.isPresent()) {
                match.setUserPronosticForCurrentUser(p.get());
            } else {
                UserPronostic prono = new UserPronostic();
                try {
                    prono.setMatch(match.clone());
                }catch(CloneNotSupportedException ex){
                    //nothing
                }
                prono.setUser(currentUser.get());
                match.setUserPronosticForCurrentUser(prono);
            }
        }
        return matches;
    }

    @RequestMapping(value = "/lastMatches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getLastMatches() {
        log.debug("REST request to get 5 last Matches");
        ZonedDateTime today = ZonedDateTime.now();
        List<Match> matches = matchRepository.queryFirst5ByMatchDateBefore(today);
        return matches;
    }

    @RequestMapping(value = "/eighth",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getEighthMatches(){
        List<Match> matches = matchRepository.queryByMatchTypeOrderByMatchDateAsc(MatchType.HUITIEME);
        return matches;
    }

    @RequestMapping(value = "/fourth",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Match> getFourthMatches(){
        List<Match> matches = matchRepository.queryByMatchTypeOrderByMatchDateAsc(MatchType.QUART);
        return matches;
    }



    /**
     * GET  /matches/:id : get the "id" match.
     *
     * @param id the id of the match to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the match, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/matches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Match> getMatch(@PathVariable Long id) {
        log.debug("REST request to get Match : {}", id);
        Match match = matchRepository.findOne(id);
        return Optional.ofNullable(match)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /matches/:id : delete the "id" match.
     *
     * @param id the id of the match to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/matches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        log.debug("REST request to delete Match : {}", id);
        matchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("match", id.toString())).build();
    }

}
