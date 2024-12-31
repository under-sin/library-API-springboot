package github.under_sin.libraryapi.repository;

import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    // query method
    List<Livro> findByAutor(Autor autor);
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);
    List<Livro> findByTituloOrGenero(String titulo, GeneroLivro genero);
    Optional<Livro> findByIsbn(String isbn);

    // JPQL -> referencia as entidades e as propriedades da classe e não do banco de dados
    @Query(" select l from Livro as l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdenandoPorTituloAndPreco();

    @Query(" select a from Livro l join l.autor a ")
    List<Autor> listarAutoresPorLivros();

    // usando parâmetros nas querys
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")
    List<Livro> findByGenero(
            @Param("genero") GeneroLivro genero,
            @Param("paramOrdenacao") String nomePropriedade
    );

    // Para query que fazem alterações no banco de dados é preciso adicionar essas anotations para funcionar.
    @Modifying
    @Transactional
    @Query("delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro genero);

    @Modifying
    @Transactional
    @Query("update Livro set dataPublicacao = ?1")
    void updateDataPublicacao(LocalDate data);

    boolean existsByAutor(Autor autor);
}
