package github.under_sin.libraryapi.repository;

import github.under_sin.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    List<Autor> findByNome(String nome);
    List<Autor> findByNascionalidade(String nascionalidade);
    List<Autor> findByNomeAndNascionalidade(String nome, String nascionalidade);
    Optional<Autor> findByNomeAndNascionalidadeAndDataNascimento(
            String nome, String nascionalidade, LocalDate dataNascimento
    );
}
