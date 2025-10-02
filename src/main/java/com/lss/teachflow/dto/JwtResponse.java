package com.lss.teachflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private int id;
    private String username;
    private String email;

    public JwtResponse(String accessToken, int id) {
        this.token = accessToken;
        this.id = id;
    }
}