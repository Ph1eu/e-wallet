package com.user_management.Configuration;

import com.user_management.Service.UserDetailServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class WebsecurityConfig {

    @Autowired
    UserDetailServiceImpl userDetailsService;



    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        //create database authentication provider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // create Bscrypt encoder
        return new BCryptPasswordEncoder();
    }
    private AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("Successful login");
            response.setStatus(HttpServletResponse.SC_OK);
        };
    }
    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("Failed login");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable().authorizeHttpRequests().
            requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated().and().
            formLogin()
            .successHandler(loginSuccessHandler())
            .failureHandler(loginFailureHandler())
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout");

    http.authenticationProvider(authenticationProvider());
    return  http.build();
//  }


}
}
