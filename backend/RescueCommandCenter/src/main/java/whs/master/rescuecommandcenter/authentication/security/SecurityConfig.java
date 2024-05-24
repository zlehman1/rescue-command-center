package whs.master.rescuecommandcenter.authentication.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.http.SessionCreationPolicy;

import whs.master.rescuecommandcenter.authentication.config.JwtAuthenticationFilterConfig;
import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;
import whs.master.rescuecommandcenter.authentication.service.impl.CustomUserDetailsServiceImpl;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    private static final String[] WHITELIST_URLS = {
            "/api/v1/rescuecommandcenter/api-docs/**",
            "/api/v1/rescuecommandcenter/swagger/**",
            "/api/v1/rescuecommandcenter/swagger-ui/**",
            "/api/v1/authentication/login",
            "/ws"
    };

    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CustomUserDetailsServiceImpl userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilterConfig jwtAuthFilter = new JwtAuthenticationFilterConfig(userDetailsService, jwtTokenService);

        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST_URLS).permitAll().anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}