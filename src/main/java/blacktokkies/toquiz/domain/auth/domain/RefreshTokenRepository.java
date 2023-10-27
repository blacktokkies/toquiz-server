package blacktokkies.toquiz.domain.auth.domain;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenDetail, Long> {
    Optional<RefreshTokenDetail> findByEmail(String email);
}
