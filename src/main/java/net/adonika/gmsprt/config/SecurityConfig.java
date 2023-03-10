package net.adonika.gmsprt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    //.ignoringAntMatchers("/h2-console/**")  // For h2-console
                    .disable()  // For RestAPI
                .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) // For h2-console
                    .and()
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
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/boards/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorization")   // For customizing. default = /oauth2/authorization
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/login/oauth2/code/*");   // For customizing. default = /login/oauth2/code/{google, github, etc ...}
        // ?????? ????????? ?????? ???????????? off
//        http
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**")  // For h2-console
//                .and()
//                .headers()
//                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) // For h2-console
//                .and()
//                .authorizeRequests()
//                .anyRequest().permitAll();

    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
