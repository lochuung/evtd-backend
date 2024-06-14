package com.evtd.dictionary.service.auth;

import com.evtd.dictionary.controller.auth.dto.AuthenticationResponse;
import com.evtd.dictionary.controller.auth.dto.LoginRequest;
import com.evtd.dictionary.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        if (user == null) {
            throw BadRequestException.message("Can not find user: " + request.getEmail());
        }

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refresh(String refreshToken) {
        final String email = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(email);
        if (user == null) {
            throw BadRequestException.message("Can not find user: " + email);
        }
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw BadRequestException.message("Refresh token is invalid");
        }

        var accessToken = jwtService.generateAccessToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
