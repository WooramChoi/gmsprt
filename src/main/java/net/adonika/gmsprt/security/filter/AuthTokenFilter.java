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
    
    private final String HEADER_REGISTRATION_ID = "RegistrationId";
    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String PREFIX_AUTHORIZATION = "Bearer ";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String registrationId = request.getHeader(HEADER_REGISTRATION_ID);
        String accessToken = request.getHeader(HEADER_AUTHORIZATION);
        
        if (StringUtils.hasText(registrationId) && StringUtils.hasText(accessToken)) {
            if (accessToken.toLowerCase().startsWith(PREFIX_AUTHORIZATION.toLowerCase())) {
                accessToken = accessToken.substring(PREFIX_AUTHORIZATION.length());
            }
            AuthTokenAuthentication authTokenPrincipal = new AuthTokenAuthentication(registrationId, accessToken);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authTokenPrincipal);
        }
        
        filterChain.doFilter(request, response);
    }

}
