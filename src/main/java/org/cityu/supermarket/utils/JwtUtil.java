package org.cityu.supermarket.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT utility
 * generates and validates JWT tokens
 * 
 * @version 0.0.1
 * @date 2025-11-29
 */
@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    // JWT secret key from config, must be at least 256 bits (32 bytes)
    @Value("${jwt.secret:supermarket-secret-key-for-jwt-token-generation-and-validation-2025}")
    private String secret;
    
    // Token expiration in milliseconds, default 24 hours
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    /**
     * Generate secret key
     */
    private SecretKey getSecretKey() {
        // Make sure key is long enough (at least 256 bits)
        if (!StringUtils.hasLength(secret) || secret.length() < 32) {
            throw new IllegalStateException("JWT secret is missing or too short. Please set JWT_SECRET to at least 32 characters.");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * generate JWT token
     * 
     * @param managerId admin ID
     * @param role role
     * @return JWT token string
     */
    public String generateToken(String managerId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("managerId", managerId);
        claims.put("role", role);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .claims(claims)
                .subject(managerId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }
    
    /**
     * get admin ID from token
     * 
     * @param token JWT Token
     * @return admin ID
     */
    public String getManagerIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("Failed to extract managerId from token", e);
            return null;
        }
    }
    
    /**
     * get role from token
     * 
     * @param token JWT Token
     * @return role
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            logger.error("Failed to extract role from token", e);
            return null;
        }
    }
    
    /**
     * validate token
     * 
     * @param token JWT Token
     * @return is valid
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Malformed token: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.warn("Token signature validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Token is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Token validation error", e);
        }
        return false;
    }
    
    /**
     * parse claims from token
     * 
     * @param token JWT Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * check if token is expired
     * 
     * @param token JWT Token
     * @return is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * refresh token (optional feature)
     * 
     * @param token old token
     * @return new token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String managerId = claims.getSubject();
            String role = claims.get("role", String.class);
            return generateToken(managerId, role);
        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            return null;
        }
    }
}
