package medicine.demo.project.security.jwt;

import medicine.demo.project.core.utilities.exceptions.TokenRefreshException;
import medicine.demo.project.core.utilities.exceptions.UserNotFoundException;
import medicine.demo.project.entity.JwtToken;
import medicine.demo.project.entity.User;
import medicine.demo.project.repository.RefreshTokenRepository;
import medicine.demo.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceJWT implements RefreshTokenService {
    @Value("${jwt.refresh.token.validity}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenServiceJWT(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<JwtToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    @Override
    public JwtToken createRefreshToken(Long userId) {
        JwtToken jwtToken = new JwtToken();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(userId + " id'ye sahip kullanıcı bulunamadı.");
        }
        jwtToken.setUser(user);
        jwtToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        jwtToken.setRefreshToken(UUID.randomUUID().toString());
        jwtToken = refreshTokenRepository.save(jwtToken);
        return jwtToken;
    }

    @Override
    public JwtToken verifyExpiration(JwtToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) <= 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshToken(), "Refresh tokenin süresi dolmuştur. Lütfen tekrar giriş yapınız.");
        }
        return token;
    }

    @Override
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Override
    public Long getIdByUsername(String username) {
        Long id = userRepository.findByUsername(username).getId();
        return id;
    }

}
