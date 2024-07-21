package whs.master.rescuecommandcenter.authentication.service.implementation;

import whs.master.rescuecommandcenter.authentication.dto.request.LoginRequestDto;
import whs.master.rescuecommandcenter.authentication.service.AuthenticationService;
import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.BOSOrganizationRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.DistrictRepository;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserRepository userRepository;

    private final BOSOrganizationRepository organizationRepository;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationProvider authenticationProvider;

    private final DistrictRepository districtRepository;

    public AuthenticationServiceImplementation(
            UserRepository userRepository,
            BOSOrganizationRepository organizationRepository,
            JwtTokenService jwtTokenService,
            AuthenticationProvider authenticationProvider,
            DistrictRepository districtRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationProvider = authenticationProvider;
        this.districtRepository = districtRepository;
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

        User user = userRepository.findByUsername(loginRequestDto.getUsername());

        if (user == null)
            return "";

        if(!userRepository.isActive(loginRequestDto.getUsername()))
            return null;

        BOSOrganization organization = organizationRepository.findByUserUsername(user.getUsername());

        String district = districtRepository.findDistrictNameByUsername(user.getUsername());


        return jwtTokenService.generateToken(user.getUsername(), user.getRoles(), organization, district);
    }
}