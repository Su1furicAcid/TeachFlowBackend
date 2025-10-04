package com.lss.teachflow.controller;

import com.lss.teachflow.dto.JwtResponse;
import com.lss.teachflow.dto.LoginRequest;
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> tokens = jwtUtils.generateTokens(authentication);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        return ResponseEntity.ok(new JwtResponse(
                accessToken,
                refreshToken,
                userService.getUserIdByUsername(loginRequest.getUsername())
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }
        userService.registerUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) throws Exception {
        if (jwtUtils.validateJwtToken(refreshToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);
            Map<String, String> tokens = jwtUtils.generateTokens(authentication);
            String newAccessToken = tokens.get("accessToken");
            String newRefreshToken = tokens.get("refreshToken");
            return ResponseEntity.ok(new JwtResponse(
                    newAccessToken,
                    newRefreshToken,
                    userService.getUserIdByUsername(username)
            ));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Invalid refresh token!");
        }
    }
}
