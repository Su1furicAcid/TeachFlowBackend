package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.JwtResponse;
import com.lss.teachflow.dto.LoginRequest;
import com.lss.teachflow.dto.RefreshRequest;
import com.lss.teachflow.dto.SignupRequest;
import com.lss.teachflow.security.JwtUtils;
import com.lss.teachflow.service.UserService;
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
@RequestMapping("/api/auth")
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
