package github.under_sin.libraryapi.service;

import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.repository.AutorRepository;
import github.under_sin.libraryapi.repository.LivroRepository;
import github.under_sin.libraryapi.validator.LivroValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
}
