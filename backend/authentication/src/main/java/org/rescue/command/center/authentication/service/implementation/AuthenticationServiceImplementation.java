package org.rescue.command.center.authentication.service.implementation;

import org.rescue.command.center.authentication.dto.request.LoginRequestDto;

import org.rescue.command.center.base.authentication.service.JwtTokenService;
import org.rescue.command.center.base.emergencycallsystem.model.BOSOrganization;
import org.rescue.command.center.base.emergencycallsystem.repository.BOSOrganizationRepository;
import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.UserRepository;

import org.rescue.command.center.authentication.service.AuthenticationService;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserRepository userRepository;

    private final BOSOrganizationRepository organizationRepository;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationProvider authenticationProvider;

    public AuthenticationServiceImplementation(
            UserRepository userRepository,
            BOSOrganizationRepository organizationRepository,
            JwtTokenService jwtTokenService,
            AuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        final Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<User> user = userRepository.findByUsername(loginRequestDto.getUsername());

        if (user.isEmpty())
            return "";

        BOSOrganization organization = organizationRepository.findByUserUsername(user.get().getUsername());

        return jwtTokenService.generateToken(user.get().getUsername(), user.get().getRoles(), organization);
    }
}