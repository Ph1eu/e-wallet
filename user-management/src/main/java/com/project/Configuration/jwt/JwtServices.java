package com.project.Configuration.jwt;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.project.Service.CustomUserDetail;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

	public String generateToken(CustomUserDetail userDetails ){

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
				setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	  }
	public String extractIdEmail(String token){
		return extractClaim(token, claims -> claims.get("id_email", String.class));

	}
	public String extractRole(String token){
		return extractClaim(token, claims -> claims.get("role", String.class));

	}
	public boolean validateToken(String token, UserDetails userDetails) {
			final String username = extractUsername(token);
			return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
		}

	private boolean isTokenExpired(String token) {

		if (extractExpiration(token).after(new Date())){
			return true;
		}
		else{
			logger.error("JWT token is expired");
			return false;
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
