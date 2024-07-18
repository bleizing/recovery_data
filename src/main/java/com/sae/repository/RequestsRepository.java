package com.sae.repository;

import com.sae.entity.Requests;
import com.sae.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    Optional<Requests> findFirstByUsersAndId(Users users, String Id);
}
