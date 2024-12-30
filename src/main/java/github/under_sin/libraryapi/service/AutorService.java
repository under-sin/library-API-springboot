package github.under_sin.libraryapi.service;

import github.under_sin.libraryapi.exception.OperacaoNaoPermitidaException;
import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.repository.AutorRepository;
import github.under_sin.libraryapi.repository.LivroRepository;
import github.under_sin.libraryapi.validator.AutorValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final AutorValidator validator;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository, AutorValidator validator) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.validator = validator;
    }

    public Autor salvar(Autor autor) {
        validator.validar(autor);
        return autorRepository.save(autor);
    }

    public void atualizar(Autor autor) {
        validator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(String idAutor) {
        return autorRepository.findById(UUID.fromString(idAutor));
    }

    public void deletar(Autor autor) {
        if (existeLivroParaAutor(autor)) {
            throw new OperacaoNaoPermitidaException("Não é permitido excluir um autor que possui livro cadastrados");
        }
        autorRepository.delete(autor);
    }

    public List<Autor> pesquisar(String nome, String nascionalidade) {
        if (nome != null && nascionalidade != null)
            return autorRepository.findByNomeAndNascionalidade(nome, nascionalidade);

        if (nome != null)
            return autorRepository.findByNome(nome);

        if (nascionalidade != null)
            return autorRepository.findByNascionalidade(nascionalidade);

        return autorRepository.findAll();
    }

    public List<Autor> pesquisaByExample(String nome, String nascionalidade) {
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNascionalidade(nascionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "idUsuario", "dataNascimento") // ignora os campos passados
                .withIgnoreCase() // ignora os cases (tudo lowercase)
                .withIgnoreNullValues() // faz a pequisa apenas pelos campos passados no objeto (nome, nascionalidade)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // %like%
        Example<Autor> autorExample = Example.of(autor, matcher);
        return autorRepository.findAll(autorExample);
    }

    public boolean existeLivroParaAutor(Autor autor) {
        boolean resultado = livroRepository.existsByAutor(autor);
        return resultado;
    }
}
