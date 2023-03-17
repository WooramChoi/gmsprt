package net.adonika.gmsprt.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import net.adonika.gmsprt.security.model.AuthTokenAuthentication;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String registrationId = request.getHeader("RegistrationId");
        String accessToken = request.getHeader("AccessToken");
        
        if (StringUtils.hasText(registrationId) && StringUtils.hasText(accessToken)) {
            AuthTokenAuthentication authTokenPrincipal = new AuthTokenAuthentication(registrationId, accessToken);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authTokenPrincipal);
        }
        
        filterChain.doFilter(request, response);
    }

}
