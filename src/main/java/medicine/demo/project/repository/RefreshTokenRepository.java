package medicine.demo.project.repository;

import medicine.demo.project.entity.JwtToken;
import medicine.demo.project.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<JwtToken, Long> {
    Optional<JwtToken> findByRefreshToken(String token);

    int deleteByUser(User user);
}
