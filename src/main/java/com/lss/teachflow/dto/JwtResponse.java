package com.lss.teachflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}