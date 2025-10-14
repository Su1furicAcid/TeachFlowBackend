package com.lss.teachflow.dto;

import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String newUsername;
    private String newEmail;
}
