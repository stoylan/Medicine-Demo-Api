package medicine.demo.project.security.jwt;

import medicine.demo.project.entity.JwtToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<JwtToken> findByToken(String token);
    JwtToken createRefreshToken(Long userId);
    JwtToken verifyExpiration(JwtToken token);
    int deleteByUserId(Long userId);
    Long getIdByUsername(String username);
}
