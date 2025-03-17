package com.agrocredito.domain.model.repository.auth;

import com.agrocredito.domain.model.entity.auth.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository  extends JpaRepository<TokenEntity, Integer> {

    @Query(value = """
      select t from TokenEntity t inner join UserEntity u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.isRevoked = false or t.isRevoked = false)\s
      """)
    List<TokenEntity> findAllValidTokenByUser(Integer id);

    Optional<TokenEntity> findByToken(String token);
}