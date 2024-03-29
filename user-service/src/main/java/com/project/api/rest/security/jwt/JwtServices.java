package com.project.api.rest.security.jwt;

import com.project.api.common.error.input_validation.ExpiredJwtException;
import com.project.api.common.error.input_validation.InvalidJwtException;
import com.project.api.rest.security.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServices {
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);
    @Value("${ph1eu.appprop.jwtSecret}")
    private String jwtSecret;

    @Value("${ph1eu.appprop.jwtExpirationMs}")
    private int jwtExpirationMs;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUserDetail userDetails) {

        return buildToken(new HashMap<>(), userDetails);
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            CustomUserDetail userDetails
    ) {
        extraClaims.put("role", userDetails.getRole()); // Add the "role" claim

        return Jwts
                .builder()
                .setClaims(extraClaims).
                setId(userDetails.getID()).
                setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractIdEmail(String token) {
        return extractClaim(token, claims -> claims.get("id_email", String.class));

    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (!username.equals(userDetails.getUsername())) {
            throw new InvalidJwtException("INVALID JWT");
        }
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        if (extractExpiration(token).after(new Date())) {
            return true;
        } else {
            logger.error("JWT token is expired");
            throw new ExpiredJwtException("EXPIRED JWT");
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
