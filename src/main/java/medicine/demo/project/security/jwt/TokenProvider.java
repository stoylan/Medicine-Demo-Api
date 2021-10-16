package medicine.demo.project.security.jwt;

import medicine.demo.project.core.utilities.exceptions.JwtTokenException;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    @Value("${jwt.refresh.token.validity}")
    public long REFRESH_TOKEN_VALIDITY;

    @Value("${jwt.signing.key}")
    public String SIGNING_KEY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    private @NonNull final RefreshTokenService refreshTokenService;

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        refreshTokenService.createRefreshToken(refreshTokenService.getIdByUsername(authentication.getName()));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();

    }
    public String generateTokenFromUsername(String username) {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).claim(AUTHORITIES_KEY,authorities)
                .setExpiration(new Date((new Date()).getTime() + TOKEN_VALIDITY)).signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(jwt);
            return true;
        }
        catch (MalformedJwtException e) {
            throw new JwtTokenException("Geçersiz JWT tokeni = "+jwt);
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException("JWT tokeninin süresi dolmuş = "+jwt);
        } catch (UnsupportedJwtException e) {
            throw new JwtTokenException("JWT tokeni desteklenmiyor = "+jwt);
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException("JWT claimsi boş = "+jwt);
        }
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(jwt).getBody().getSubject();

    }
}
