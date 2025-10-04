package com.lss.teachflow.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private int id;

    public JwtResponse(String accessToken, String refreshToken, int id) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
    }
}