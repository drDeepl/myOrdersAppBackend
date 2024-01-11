package ru.myorder.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.myorder.services.UserDetailsImpl;

import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationms}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImpl accountPrincipal) {
        LOGGER.info("GENERATE JWT TOKEN");
        boolean isAdminAccount = accountPrincipal.getAuthorities().toArray()[0].toString() == "ROLE_ADMIN";
        long accountId = accountPrincipal.getId();
        return generateTokenFromUsername(accountPrincipal.getUsername(), isAdminAccount, accountId);
    }

    public String generateTokenFromUsername(String username, boolean isAdmin, Long accountId) {
        LOGGER.info("GENERATE TOKEN FROM USERNAME");
        final Date createdDate = new Date();
        final Date expirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", accountId);
        claims.put("isAdmin", isAdmin);
        return Jwts.builder()
                .setSubject(username).setIssuedAt(createdDate)
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        LOGGER.info("GET USERNAME FROM JWT TOKEN");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
//            Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            throw new ExpiredJwtException(jws.getHeader(), jws.getBody(), e.getMessage() );
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public boolean getIsAdminFromAccountDetails(UserDetailsImpl accountDetails) {
        String accountRole = accountDetails.getAuthorities().toArray()[0].toString();
        boolean isAdmin = StringUtils.equals(accountRole, "ROLE_ADMIN");
        return isAdmin;
    }

    public Long getAccountIdFromJWT(String token){

        String accountId = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("id").toString();

        return Long.parseLong(accountId);
    }

    private Claims extractAllClaims(String token){
        LOGGER.info("EXTRACT ALL CLAIMS");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public Boolean getIsAdmin(String token){
        LOGGER.info("GET IS ADMIN");
        final Claims claims = extractAllClaims(token);
        return (boolean) claims.get("isAdmin");
    }


}