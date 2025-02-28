package com.postiy.postify.AuditingConfiguration;

import com.postiy.postify.jwt.JwtRequestFilter;
import com.postiy.postify.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures Spring Security for the application, enabling JWT-based authentication,
 * stateless session management, and role-based access control. This class sets up
 * security filters, authentication mechanisms, and endpoint authorization rules.
 * Integrates Swagger (OpenAPI) endpoints for API documentation.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructs the SecurityConfiguration with required dependencies.
     *
     * @param userDetailsService The custom user details service for loading user data.
     * @param jwtRequestFilter   The filter for processing JWT authentication in requests.
     */
    @Autowired
    public SecurityConfiguration(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Provides a BCrypt password encoder for hashing and verifying user passwords.
     *
     * @return A PasswordEncoder instance using BCrypt algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager bean for authenticating users.
     *
     * @param authenticationConfiguration The configuration providing access to the AuthenticationManager.
     * @return The configured AuthenticationManager.
     * @throws Exception If there is an error retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain for HTTP requests, defining authentication,
     * authorization rules, session management, and Swagger endpoint access.
     *
     * @param http The HttpSecurity object to configure security settings.
     * @return The constructed SecurityFilterChain.
     * @throws Exception If there is an error configuring the security chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permit Swagger UI and OpenAPI endpoints
                                                 // Existing rules
                                .requestMatchers(HttpMethod.POST, "/users", "/auth/**").permitAll()
                                .requestMatchers("/posts/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}