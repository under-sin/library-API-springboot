package github.under_sin.libraryapi.service;

import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import github.under_sin.libraryapi.repository.LivroRepository;
import github.under_sin.libraryapi.repository.specs.LivroSpecs;
import github.under_sin.libraryapi.validator.LivroValidator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static github.under_sin.libraryapi.repository.specs.LivroSpecs.*;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;

    public LivroService(LivroRepository livroRepository, LivroValidator livroValidator) {
        this.livroRepository = livroRepository;
        this.livroValidator = livroValidator;
    }

    public Livro salvar(Livro livro) {
        livroValidator.validar(livro);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(String id) {
        return livroRepository.findById(UUID.fromString(id));
    }

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public List<Livro> pesquisa(
        String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao) {

        Specification<Livro> specs = Specification
                .where(((root, query, cb) -> cb.conjunction()));

        if(isbn != null)
            specs = specs.and(isbnEqual(isbn));

        if(titulo != null)
            specs = specs.and(tituloLike(titulo));

        if(genero != null)
            specs = specs.and(generoEqual(genero));

        return livroRepository.findAll(specs);
    }
}
