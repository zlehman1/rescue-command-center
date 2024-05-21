package org.rescue.command.center.base.authentication.service;

import io.jsonwebtoken.Claims;
import org.rescue.command.center.base.emergencycallsystem.enums.BOSOrganizationEnum;
import org.rescue.command.center.base.emergencycallsystem.model.BOSOrganization;
import org.rescue.command.center.base.userManagement.model.Role;

import java.util.Set;
import java.util.function.Function;

public interface JwtTokenService {
    public String generateToken(String username, Set<Role> authorities, BOSOrganization organization, String districtName);

    public String extractUsernameFromToken(String token);

    String extractDistrictNameFromToken(String token);

    public Set<Role> extractRolesFromToken(String token);

    public BOSOrganizationEnum extractBOSOrganizationFromToken(String token);

    public <T> T getClaims(String token, Function<Claims, T> resolver);

    public boolean isTokenExpired(String token);
}
