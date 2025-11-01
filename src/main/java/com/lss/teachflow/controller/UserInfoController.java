package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.service.UserService;
import com.lss.teachflow.dto.UserInfoResponse;
import com.lss.teachflow.dto.UpdateUserInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseBody<UserInfoResponse> getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        String userEmail = userService.getUserEmailByUserId(userService.getUserIdByUsername(userName));
        UserInfoResponse userInfo = new UserInfoResponse(userName, userEmail);
        return ResponseBody.success(userInfo);
    }

    @PutMapping("/me")
    public ResponseBody<?> updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserInfo(userDetails.getUsername(), request.getNewUsername(), request.getNewEmail());
        return ResponseBody.success(null, "User info updated successfully!");
    }
}
