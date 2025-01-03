package github.under_sin.libraryapi.repository;

import github.under_sin.libraryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByLogin(String login);
    boolean existsByLogin(String login);
}
