package github.under_sin.libraryapi.validator;

import github.under_sin.libraryapi.exception.OperacaoNaoPermitidaException;
import github.under_sin.libraryapi.exception.RegistroDuplicadoException;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.repository.LivroRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class LivroValidator {

    private final LivroRepository livroRepository;

    public LivroValidator(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public void validar(Livro livro) {
        if(existeLivroComIsbn(livro))
            throw new RegistroDuplicadoException("Isbn Duplicado");
        if(validarPreco(livro))
            throw new OperacaoNaoPermitidaException("O preço é obrigatório para publicação a partir de 2020");
        if(validarSeTemAutor(livro))
            throw new OperacaoNaoPermitidaException("Não foi possível encontrar o autor");
    }

    private boolean existeLivroComIsbn(Livro livro) {
        Optional<Livro> livroOptional = livroRepository.findByIsbn(livro.getIsbn());
        if (livro.getId() == null)
            return livroOptional.isPresent();

        return livroOptional.isPresent() && !livro.getId().equals(livroOptional.get().getId());
    }

    private boolean validarPreco(Livro livro) {
        if (livro.getDataPublicacao().getYear() >= 2020)
            return !(livro.getPreco() != null && livro.getPreco().compareTo(BigDecimal.ZERO) > 0);

        return false;
    }

    private boolean validarSeTemAutor(Livro livro) {
        return livro.getAutor() == null;
    }
}
