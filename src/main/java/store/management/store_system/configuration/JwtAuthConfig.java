package store.management.store_system.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import store.management.store_system.service.JwtService;
import store.management.store_system.service.MyUserDetailsService;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class JwtAuthConfig {
    private final MyUserDetailsService userDetailsService;

    private final JwtService jwtService;

    public void handleBearerToken(String authorizationHeader, HttpServletRequest request) {

        String jwt = authorizationHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }


}
