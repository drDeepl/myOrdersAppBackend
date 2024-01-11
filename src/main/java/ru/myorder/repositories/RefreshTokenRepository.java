package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.myorder.models.RefreshToken;
import ru.myorder.models.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query(value="SELECT * FROM refresh_token WHERE user_id = :userId", nativeQuery = true)
    Optional<RefreshToken> findByUserId(@Param("userId") Long userId);

    @Modifying
    int deleteByUser(User user);

}
