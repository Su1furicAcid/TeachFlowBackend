package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.JwtResponse;
import com.lss.teachflow.dto.LoginRequest;
import com.lss.teachflow.dto.RefreshRequest;
import com.lss.teachflow.dto.SignupRequest;
import com.lss.teachflow.security.JwtUtils;
import com.lss.teachflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Tag(name = "用户认证", description = "用户登录、注册和刷新token")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/signin")
    @Operation(summary = "用户登录", description = "使用用户名和密码进行认证，成功后返回JWT")
    public ResponseBody<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> tokens = jwtUtils.generateTokens(authentication);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        return ResponseBody.success(new JwtResponse(
                accessToken,
                refreshToken,
                userService.getUserIdByUsername(loginRequest.getUsername())
        ));
    }

    @PostMapping("/signup")
    @Operation(summary = "用户注册", description = "注册新用户")
    public ResponseBody<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseBody.error("400", "Error: Username is already taken!");
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseBody.error("400", "Error: Email is already in use!");
        }
        userService.registerUser(signUpRequest);
        return ResponseBody.success(null, "User registered successfully!");
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用refresh token获取新的access token和refresh token")
    public ResponseBody<?> refreshToken(@Valid @RequestBody RefreshRequest refreshRequest) throws Exception {
        String refreshToken = refreshRequest.getRefreshToken();
        if (jwtUtils.validateJwtToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            Map<String, String> tokens = jwtUtils.generateTokens(authentication);
            String newAccessToken = tokens.get("accessToken");
            String newRefreshToken = tokens.get("refreshToken");
            return ResponseBody.success(new JwtResponse(
                    newAccessToken,
                    newRefreshToken,
                    userService.getUserIdByUsername(username)
            ));
        } else {
            return ResponseBody.error("400", "Error: Invalid refresh token!");
        }
    }
}
