package com.lss.teachflow.service;

import com.lss.teachflow.dto.SignupRequest;

public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void registerUser(SignupRequest signUpRequest);
    Long getUserIdByUsername(String username);
    String getUserEmailByUserId(Long id);
    void updateUserInfo(String currentUsername, String newUsername, String newEmail);
}
