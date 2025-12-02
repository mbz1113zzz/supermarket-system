package org.cityu.supermarket.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cityu.supermarket.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT auth filter
 * Intercepts all requests, validates JWT Token
 * 
 * @version 0.0.1
 * @date 2025-11-29
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtUtil jwtUtil;
    
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Get JWT Token from request header
            String token = getJwtFromRequest(request);
            
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                // Token valid, set authentication info
                String managerId = jwtUtil.getManagerIdFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                
                if (managerId != null) {
                    // Map Chinese roles to Spring Security standard roles
                    String securityRole = mapRoleToSecurityRole(role);
                    
                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    managerId,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(securityRole))
                            );
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set to Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    logger.debug("Set authentication principal: managerId={}, role={}", managerId, role);
                }
            }
        } catch (Exception e) {
            logger.error("Unable to set authentication context", e);
        }
        
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extract JWT Token from request header
     * Supports two formats:
     * 1. Authorization: Bearer <token>
     * 2. Authorization: <token>
     * 
     * @param request HTTP request
     * @return JWT Token, or null if not present
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken)) {
            // If starts with "Bearer ", strip the prefix
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            // Otherwise return as-is
            return bearerToken;
        }
        
        return null;
    }
    
    /**
     * Map database roles to Spring Security standard roles
     * 
     * @param role Role from database (Admin, Operator, Finance - stored as Chinese values)
     * @return Spring Security role (ROLE_ADMIN, ROLE_OPERATOR, ROLE_FINANCE)
     */
    private String mapRoleToSecurityRole(String role) {
        if (role == null) {
            return "ROLE_USER";
        }
        return switch (role.trim().toUpperCase()) {
            case "管理员", "ADMIN", "ROLE_ADMIN" -> "ROLE_ADMIN";
            case "操作员", "OPERATOR", "ROLE_OPERATOR" -> "ROLE_OPERATOR";
            case "财务", "FINANCE", "ROLE_FINANCE" -> "ROLE_FINANCE";
            default -> "ROLE_USER";
        };
    }
}
