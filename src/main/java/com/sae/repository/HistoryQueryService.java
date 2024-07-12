package com.sae.repository;

import com.sae.entity.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryQueryService extends JpaRepository<Requests, Long> {

    List<Requests> findAllByUsers_username(String username);
}
