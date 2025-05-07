package store.management.store_system.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestAuthFactory {

    private final JwtAuthConfig jwtAuthConfig;

    public void requestAuth(String authorizationHeader, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (authorizationHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            if(authorizationHeader.startsWith("Bearer ")){
                jwtAuthConfig.handleBearerToken(authorizationHeader, request);
            }
        }catch (Exception ex){
            log.error("Authentication error: {}", ex.getMessage());
        }
    }
}
