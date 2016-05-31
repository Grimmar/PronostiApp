package com.pronostiapp.com.repository;

import com.pronostiapp.com.domain.UserPronostic;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPronostic entity.
 */
@SuppressWarnings("unused")
public interface UserPronosticRepository extends JpaRepository<UserPronostic,Long> {

    @Query("select userPronostic from UserPronostic userPronostic where userPronostic.user.login = ?#{principal.username}")
    List<UserPronostic> findByUserIsCurrentUser();

}
