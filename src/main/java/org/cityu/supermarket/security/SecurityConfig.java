package org.cityu.supermarket.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.cityu.supermarket.common.constants.ResultCode;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security config
 * 
 * @version 0.0.1
 * @date 2025-11-29
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
    private final List<String> allowedOrigins;
    
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            ObjectMapper objectMapper,
            @Value("${cors.allowed-origins:*}") String allowedOrigins
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
        this.allowedOrigins = parseAllowedOrigins(allowedOrigins);
    }
    
    /**
     * Password encoder (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Security filter chain config
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (frontend-backend separation uses JWT, no need for CSRF)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Enable CORS with custom config
            .cors(Customizer.withDefaults())
            
            // Session management: stateless (using JWT, no Session needed)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Preflight requests pass through to avoid CORS blocking
                .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name())).permitAll()
                
                // ==================== Public APIs (no auth required) ====================
                .requestMatchers(
                    // Admin login endpoint
                    new AntPathRequestMatcher("/managerLogin"),
                    // Swagger / Knife4j docs
                    new AntPathRequestMatcher("/doc.html"),
                    new AntPathRequestMatcher("/swagger-ui.html"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/swagger-resources/**"),
                    new AntPathRequestMatcher("/v3/api-docs/**"),
                    new AntPathRequestMatcher("/webjars/**"),
                    new AntPathRequestMatcher("/knife4j/**"),
                    // System resources
                    new AntPathRequestMatcher("/favicon.ico"),
                    new AntPathRequestMatcher("/error")
                ).permitAll()
                
                // ==================== All business APIs require ADMIN role ====================
                // Member management
                .requestMatchers(new AntPathRequestMatcher("/member/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/members/**")).hasRole("ADMIN")
                // Card management
                .requestMatchers(new AntPathRequestMatcher("/card/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/cards/**")).hasRole("ADMIN")
                // Order management
                .requestMatchers(new AntPathRequestMatcher("/order/**")).hasRole("ADMIN")
                // Transaction records
                .requestMatchers(new AntPathRequestMatcher("/record/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/records/**")).hasRole("ADMIN")
                // Product management
                .requestMatchers(new AntPathRequestMatcher("/product/**")).hasRole("ADMIN")
                // Procurement management
                .requestMatchers(new AntPathRequestMatcher("/procurement/**")).hasRole("ADMIN")
                // Marketing management
                .requestMatchers(new AntPathRequestMatcher("/marketing/**")).hasRole("ADMIN")
                // Statistics reports
                .requestMatchers(new AntPathRequestMatcher("/statistics/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getStatisticData")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/requestDealData")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getSchart1Data")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getRecentMembers")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getMemberIntegralRanking")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getBalanceDistribution")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getTodayNewMemberCount")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/getPendingTasks")).hasRole("ADMIN")
                
                // All other requests require ADMIN role
                .anyRequest().hasRole("ADMIN")
            )
            
            // Exception handling
            .exceptionHandling(exception -> exception
                // Unauthenticated handler
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    
                    SupermarketResult<?> result = SupermarketResult.failure(
                        ResultCode.UNAUTHORIZED,
                        "Not authenticated or token expired. Please sign in again."
                    );
                    
                    PrintWriter writer = response.getWriter();
                    writer.write(objectMapper.writeValueAsString(result));
                    writer.flush();
                })
                
                // Access denied handler
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    
                    SupermarketResult<?> result = SupermarketResult.failure(
                        ResultCode.NO_PERMISSION,
                        "Insufficient permissions to access this resource."
                    );
                    
                    PrintWriter writer = response.getWriter();
                    writer.write(objectMapper.writeValueAsString(result));
                    writer.flush();
                })
            )
            
            // Add JWT filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * Custom CORS config - lets frontend domain access and exposes needed response headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (allowedOrigins.isEmpty()) {
            configuration.addAllowedOriginPattern("*");
        } else {
            allowedOrigins.forEach(configuration::addAllowedOriginPattern);
        }
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Origin", "Content-Type", "Authorization", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static List<String> parseAllowedOrigins(String origins) {
        if (!StringUtils.hasText(origins)) {
            return List.of();
        }
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }
}
