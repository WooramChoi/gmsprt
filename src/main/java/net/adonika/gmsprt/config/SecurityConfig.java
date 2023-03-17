package net.adonika.gmsprt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import net.adonika.gmsprt.security.filter.AuthTokenFilter;
import net.adonika.gmsprt.security.service.AuthTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final AuthTokenAuthenticationProvider authTokenAuthenticationProvider;
    
    public SecurityConfig(AuthTokenAuthenticationProvider authTokenAuthenticationProvider) {
        this.authTokenAuthenticationProvider = authTokenAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()    // Http Basic Auth 기반 로그인
                .disable()
            .csrf()         // Form 에 csrf 값을 파라미터로 추가하여, 비정상적인 접근을 막는 Spring 솔루션
                .disable()
            .formLogin()    // 로그인 화면
                .disable()
            .logout()       // 로그아웃
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS = 메모리에 세션을 올려놓지 않음. 요청마다 인증정보를 포함해야함
                .and()
            .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) // For h2-console
                .and()
            .addFilterAfter(authTokenFilter(), CsrfFilter.class)
            .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/h2-console/**",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js").permitAll()
                .antMatchers("/security/login").permitAll()
                .antMatchers("/security/**").authenticated()
                .anyRequest().permitAll();
//                .antMatchers("/oauth2/**").permitAll()
//                .anyRequest().authenticated();
//                .oauth2Login()
//                    .authorizationEndpoint()
//                        .baseUri("/oauth2/authorization")   // For customizing. default = /oauth2/authorization
//                        .and()
//                    .redirectionEndpoint()
//                        .baseUri("/login/oauth2/code/*");   // For customizing. default = /login/oauth2/code/{google, github, etc ...}
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(authTokenAuthenticationProvider);
    }
    
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
