package com.nikita.testproject.config;

import com.nikita.testproject.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtFilter jwtFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.httpBasic().disable().cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/api/auth/**","/activate/*","password/**").permitAll()
                .antMatchers("/upload-file/*","/user/**", "/download").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll() //
            .antMatchers(HttpMethod.POST, "/register").permitAll()
               // For Test on Browser
            // Need authentication.

            //
            // Add Filter 1 - JWTLoginFilter
            //
                .antMatchers("/register", "/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
