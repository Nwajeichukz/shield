package store.management.store_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    String SECRET_KEY;

    public String extractUsername(String token){return  extractClaim(token, Claims::getSubject);}

    public Date extractExpiration(String token){return  extractClaim(token, Claims::getExpiration);}

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        }
        return Collections.emptyList();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, List<String> role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", role);
        return createToken(claims, userDetails.getUsername());
    }


    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}