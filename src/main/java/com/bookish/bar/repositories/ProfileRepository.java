package com.bookish.bar.repositories;

import com.bookish.bar.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByProfilePictureUrl(String profilePictureUrl);
//    @Query("SELECT p FROM Profile p WHERE p.user.username = :username")
    Optional<Profile> findByUser_Username(String username);

}
