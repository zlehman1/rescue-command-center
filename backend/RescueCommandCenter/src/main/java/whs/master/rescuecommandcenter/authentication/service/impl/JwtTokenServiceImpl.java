package whs.master.rescuecommandcenter.authentication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.usermanagement.model.Role;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtTokenServiceImpl implements JwtTokenService {

    private String secretKey = "NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3Nl";
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 86400; // 24 hours

    public String generateToken(String username, Set<Role> authorities, BOSOrganization organization, String districtName) {
        return Jwts.builder().subject(username)
                .claim("roles", authorities)
                .claim("organization", organization.getName())
                .claim("district", districtName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsernameFromToken(String token) {
        if (isTokenExpired(token)) {
            return null;
        }
        return getClaims(token, Claims::getSubject);
    }

    public Set<Role> extractRolesFromToken(String token) {
        if (isTokenExpired(token)) {
            return new HashSet<>();
        }

        Claims claims = getClaims(token, Function.identity());
        List<?> rolesList = claims.get("roles", List.class);
        Set<Role> roles = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();

        if (rolesList != null) {
            for (Object item : rolesList) {
                roles.add(mapper.convertValue(item, Role.class));
            }
        }

        return roles;
    }

    public BOSOrganizationEnum extractBOSOrganizationFromToken(String token){
        if (isTokenExpired(token)) {
            return BOSOrganizationEnum.NOTDEFINED;
        }

        Claims claims = getClaims(token, Function.identity());
        return BOSOrganizationEnum.valueOf(claims.get("organization", String.class).toUpperCase());
    }

    public String extractDistrictNameFromToken(String token){
        if (isTokenExpired(token)) {
            return "";
        }

        Claims claims = getClaims(token, Function.identity());
        return claims.get("district", String.class);
    }

    public <T> T getClaims(String token, Function<Claims, T> resolver) {
        return resolver.apply(Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}