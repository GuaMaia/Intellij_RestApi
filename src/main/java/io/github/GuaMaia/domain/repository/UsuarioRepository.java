package io.github.GuaMaia.domain.repository;

import io.github.GuaMaia.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario , Integer> {

    // Seria o if do Delphi
    Optional<Usuario> findByLogin(String login);


}
