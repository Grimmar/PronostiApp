package com.pronostiapp.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pronostiapp.com.domain.Match;
import com.pronostiapp.com.domain.User;
import com.pronostiapp.com.domain.UserPronostic;
import com.pronostiapp.com.repository.UserPronosticRepository;
import com.pronostiapp.com.security.SecurityUtils;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserPronostic.
 */
@RestController
@RequestMapping("/api")
public class UserPronosticResource {

    private final Logger log = LoggerFactory.getLogger(UserPronosticResource.class);

    @Inject
    private UserPronosticRepository userPronosticRepository;

    /**
     * POST  /user-pronostics : Create a new userPronostic.
     *
     * @param userPronostic the userPronostic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPronostic, or with status 400 (Bad Request) if the userPronostic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-pronostics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPronostic> createUserPronostic(@RequestBody UserPronostic userPronostic) throws URISyntaxException {
        log.debug("REST request to save UserPronostic : {}", userPronostic);
        if (userPronostic.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userPronostic", "idexists", "A new userPronostic cannot already have an ID")).body(null);
        }
        UserPronostic result = userPronosticRepository.save(userPronostic);
        return ResponseEntity.created(new URI("/api/user-pronostics/" + result.getId()))
            //.headers(HeaderUtil.createEntityCreationAlert("userPronostic", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-pronostics : Updates an existing userPronostic.
     *
     * @param userPronostic the userPronostic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPronostic,
     * or with status 400 (Bad Request) if the userPronostic is not valid,
     * or with status 500 (Internal Server Error) if the userPronostic couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-pronostics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPronostic> updateUserPronostic(@RequestBody UserPronostic userPronostic) throws URISyntaxException {
        log.debug("REST request to update UserPronostic : {}", userPronostic);
        if (userPronostic.getId() == null) {
            return createUserPronostic(userPronostic);
        }

        UserPronostic result = userPronosticRepository.save(userPronostic);
        return ResponseEntity.ok()
            //.headers(HeaderUtil.createEntityUpdateAlert("userPronostic", userPronostic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-pronostics : get all the userPronostics.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userPronostics in body
     */
    @RequestMapping(value = "/user-pronostics",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserPronostic> getAllUserPronostics() {
        log.debug("REST request to get all UserPronostics");
        List<UserPronostic> userPronostics = userPronosticRepository.findAll();
        return userPronostics;
    }

    /**
     * GET  /user-pronostics/:id : get the "id" userPronostic.
     *
     * @param id the id of the userPronostic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPronostic, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-pronostics/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPronostic> getUserPronostic(@PathVariable Long id) {
        log.debug("REST request to get UserPronostic : {}", id);
        UserPronostic userPronostic = userPronosticRepository.findOne(id);
        return Optional.ofNullable(userPronostic)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-pronostics/:id : delete the "id" userPronostic.
     *
     * @param id the id of the userPronostic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-pronostics/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserPronostic(@PathVariable Long id) {
        log.debug("REST request to delete UserPronostic : {}", id);
        userPronosticRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userPronostic", id.toString())).build();
    }

}
