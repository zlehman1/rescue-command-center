package whs.master.rescuecommandcenter.usermanagement.service.implementation;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;

import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdatePasswordDto;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;
import whs.master.rescuecommandcenter.usermanagement.service.PasswordService;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository _userRepository;
    private final JwtTokenService _jwtTokenService;
    private final BCryptPasswordEncoder _passwordEncoder;

    public PasswordServiceImpl(UserRepository userRepository, JwtTokenService jwtTokenService, BCryptPasswordEncoder passwordEncoder) {
        _userRepository = userRepository;
        _jwtTokenService = jwtTokenService;
        _passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean updatePassword(String token, UpdatePasswordDto requestDto){
        String username = _jwtTokenService.extractUsernameFromToken(token);

        User user = _userRepository.findByUsername(username);

        if(!_passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword()))
            return false;

        user.setPassword(_passwordEncoder.encode(requestDto.getNewPassword()));
        _userRepository.save(user);

        return true;
    }
}
