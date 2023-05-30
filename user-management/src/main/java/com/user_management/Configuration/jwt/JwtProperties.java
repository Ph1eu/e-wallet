package com.user_management.Configuration.jwt;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.user_management.Service.CustomUserDetail;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
@Component
public class JwtProperties {
	  private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);
	  @Value("${ph1eu.appprop.jwtSecret}")
	  private String jwtSecret;

	  @Value("${ph1eu.appprop.jwtExpirationMs}")
	  private int jwtExpirationMs;

	  @Value("${ph1eu.appprop.jwtCookieName}")
	  private String jwtCookie;
	  
	  
	  public String getJwtFromCookies(HttpServletRequest request) {
		    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		    if (cookie != null) {
		      return cookie.getValue();
		    } else {
		      return null;
		    }
		  }
	  public ResponseCookie getCleanJwtCookie() {
		    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
		    return cookie;
		  }

	  public String getUserNameFromJwtToken(String token) {
		    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
		  }
	  public boolean validateJwtToken(String authToken) {
			    try {
			      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			      return true;
			    } catch (SignatureException e) {
			      logger.error("Invalid JWT signature: {}", e.getMessage());
			    } catch (MalformedJwtException e) {
			      logger.error("Invalid JWT token: {}", e.getMessage());
			    } catch (ExpiredJwtException e) {
			      logger.error("JWT token is expired: {}", e.getMessage());
			    } catch (UnsupportedJwtException e) {
			      logger.error("JWT token is unsupported: {}", e.getMessage());
			    } catch (IllegalArgumentException e) {
			      logger.error("JWT claims string is empty: {}", e.getMessage());
			    }

			    return false;
			  }
	  public String generateTokenFromUsername(String username) {  

		    return Jwts.builder()
		        .setSubject(username)
		        .setIssuedAt(new Date())
		        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).
		        signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
		        .compact();
		  }
	  public ResponseCookie generateJwtCookie(CustomUserDetail userdetail) {
		    String jwt = generateTokenFromUsername(userdetail.getUsername());
		    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
		    return cookie;
		  }
}
