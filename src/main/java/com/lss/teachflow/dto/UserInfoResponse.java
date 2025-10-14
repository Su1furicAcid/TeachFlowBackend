package com.lss.teachflow.dto;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String username;
    private String email;

    public UserInfoResponse(String userName, String userEmail) {
        this.username = userName;
        this.email = userEmail;
    }
}
