package br.com.tutoraia.repository;

import br.com.tutoraia.modelo.TokenResetSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenResetSenhaRepository extends JpaRepository<TokenResetSenha, Long> {
    Optional<TokenResetSenha> findByToken(String token);
}