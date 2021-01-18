package com.smoothstack.orchestrator.dao;

import com.smoothstack.orchestrator.entity.ConfirmationToken;
import com.smoothstack.orchestrator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByConfirmationToken(String token);
    Optional<ConfirmationToken> findByUserUserId(Long id);
}