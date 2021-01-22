package com.smoothstack.orchestrator.service;

import com.smoothstack.orchestrator.dao.ConfirmationTokenDao;
import com.smoothstack.orchestrator.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenDao confirmationTokenDao;

    void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenDao.save(confirmationToken);
    }

    void deleteConfirmationToken(Long id){
        confirmationTokenDao.deleteById(id);
    }

    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
        return confirmationTokenDao.findByConfirmationToken(token);
    }

    public Optional<ConfirmationToken> findConfirmationTokenByUserId(long id) {
        return confirmationTokenDao.findByUserUserId(id);
    }

}