package org.rescue.command.center.authentication.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final HandlerMappingIntrospector introspector;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, HandlerMappingIntrospector introspector){
        this.customUserDetailsService = customUserDetailsService;
        this.introspector = introspector;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        MvcRequestMatcher loginMatcher = new MvcRequestMatcher(introspector, "/api/v1/authentication/login");
        MvcRequestMatcher registerMatcher = new MvcRequestMatcher(introspector, "/api/v1/authentication/register/**");
        MvcRequestMatcher homeMatcher = new MvcRequestMatcher(introspector, "/api/v1/authentication/home");
        MvcRequestMatcher adminMatcher = new MvcRequestMatcher(introspector, "/admin/**");
        MvcRequestMatcher roleMatcher = new MvcRequestMatcher(introspector, "/role/**");

        MvcRequestMatcher inventoryMatcher = new MvcRequestMatcher(introspector, "/inventories/**");
        MvcRequestMatcher categoryMatcher = new MvcRequestMatcher(introspector, "/category/**");
        MvcRequestMatcher productsMatcher = new MvcRequestMatcher(introspector, "/products/**");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(loginMatcher, registerMatcher).permitAll()
                        .requestMatchers(adminMatcher).hasAnyAuthority("Admin")
                        .requestMatchers(roleMatcher).hasAnyAuthority("Admin")
                        .requestMatchers(inventoryMatcher, categoryMatcher, productsMatcher, homeMatcher).hasAnyAuthority("Admin", "User")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession(false);
                            if (session != null) {
                                session.invalidate();
                            }
                            Cookie cookie = new Cookie("JSESSIONID", null);
                            cookie.setPath(request.getContextPath());
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        })
                        .logoutSuccessUrl("/logout/page")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}