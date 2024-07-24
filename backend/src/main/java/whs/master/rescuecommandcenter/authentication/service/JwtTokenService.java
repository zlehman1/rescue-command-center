package whs.master.rescuecommandcenter.authentication.service;

import io.jsonwebtoken.Claims;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.usermanagement.model.Role;

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
