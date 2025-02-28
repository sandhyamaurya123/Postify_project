package com.postiy.postify.controller;

import com.postiy.postify.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticates a user and generates both access and refresh tokens.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        final String accessToken = jwtUtil.generateToken(new HashMap<>(), userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }

    /**
     * Refreshes an access token using a valid refresh token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isRefreshToken(refreshToken) && jwtUtil.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateToken(new HashMap<>(), userDetails);
                return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    /**
     * Verifies the validity of a token (access or refresh).
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody VerifyRequest verifyRequest) {
        String token = verifyRequest.token();
        try {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                String tokenType = jwtUtil.isRefreshToken(token) ? "refresh" : "access";
                return ResponseEntity.ok(new VerifyResponse(tokenType, username, "Valid"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}

/**
 * DTO for authentication request.
 */
record AuthenticationRequest(String username, String password) {}

/**
 * DTO for authentication response with access and refresh tokens.
 */
record AuthenticationResponse(String accessToken, String refreshToken) {}

/**
 * DTO for refresh token request.
 */
record RefreshRequest(String refreshToken) {}

/**
 * DTO for verify token request.
 */
record VerifyRequest(String token) {}

/**
 * DTO for verify token response.
 */
record VerifyResponse(String tokenType, String username, String status) {}