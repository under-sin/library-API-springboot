package github.under_sin.libraryapi.service;

import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.Usuario;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import github.under_sin.libraryapi.repository.LivroRepository;
import github.under_sin.libraryapi.security.SecurityService;
import github.under_sin.libraryapi.validator.LivroValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final SecurityService securityService;

    public LivroService(LivroRepository livroRepository, LivroValidator livroValidator, SecurityService securityService) {
        this.livroRepository = livroRepository;
        this.livroValidator = livroValidator;
        this.securityService = securityService;
    }

    public Livro salvar(Livro livro) {
        livroValidator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(String id) {
        return livroRepository.findById(UUID.fromString(id));
    }

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa(
        String isbn,
        String titulo,
        String nomeAutor,
        GeneroLivro genero,
        Integer anoPublicacao,
        Integer pagina,
        Integer tamanhoPagina) {

        Specification<Livro> specs = Specification
                .where(((root, query, cb) -> cb.conjunction()));

        if(isbn != null)
            specs = specs.and(isbnEqual(isbn));

        if(titulo != null)
            specs = specs.and(tituloLike(titulo));

        if(genero != null)
            specs = specs.and(generoEqual(genero));

        if(anoPublicacao != null)
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));

        if(nomeAutor != null)
            specs = specs.and(nomeAutorLike(nomeAutor));

        // configurando a paginação
        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);
    }

    public void atualizar(Livro livro) {
        if (livro.getId() == null)
            throw new IllegalArgumentException("Só é possível atualizar um livro cadastrado na base");

        livroValidator.validar(livro);
        livroRepository.save(livro);
    }
}
