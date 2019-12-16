package ru.itis.chat.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.itis.chat.security.filters.JwtTokenFilter;
import ru.itis.chat.security.filters.TokenAuthFilter;
import ru.itis.chat.security.provider.JwtTokenAuthenticationProvider;
import ru.itis.chat.security.provider.TokenAuthenticationProvider;


@ComponentScan("ru.itis")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private TokenAuthFilter tokenAuthFilter;

    @Autowired
    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();

        http
                .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(tokenAuthFilter, JwtTokenFilter.class)
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/users/**").hasAuthority("USER")
                .antMatchers("/refresh").hasAuthority("USER")
                .antMatchers("/messages/**").permitAll()
                .antMatchers("/signUp").permitAll()
                .antMatchers("/login").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(tokenAuthenticationProvider)
                .authenticationProvider(jwtTokenAuthenticationProvider);
    }
}
