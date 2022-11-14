package com.miniproject.demo.security;

import com.miniproject.demo.domain.dao.UserDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("600000")
    private Long expiration;

    public String generationToken(Authentication authentication){
        final UserDao userDao = (UserDao) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() * expiration);

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userDao.getId());
        claims.put("username", userDao.getUsername());
        claims.put("email", userDao.getEmail());
        claims.put("name", userDao.getName());
        claims.put("address", userDao.getAddress());
        claims.put("phone", userDao.getPhone());
        claims.put("nik", userDao.getNik());

        return Jwts.builder()
                .setId(userDao.getId().toString())
                .setSubject(userDao.getUsername())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            log.error("Invalid Jwt Signature : {}", ex.getMessage());
        } catch (MalformedJwtException ex){
            log.error("Invalid Jwt Token : {}", ex.getMessage());
        } catch (ExpiredJwtException ex){
            log.error("Invalid Jwt Token : {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported Jwt Token : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Unsupported Jwt Token : {}", ex.getMessage());
        }
        return false;
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    public Long getId(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.valueOf(claims.get("id").toString());
    }

    public String getEmail(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("email").toString();
    }

    public String getName(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("name").toString();
    }

    public String getAddress(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("address").toString();
    }

    public String getPhone(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("phone").toString();
    }

    public Integer getPoint(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Integer.valueOf(claims.get("point").toString());
    }

}
