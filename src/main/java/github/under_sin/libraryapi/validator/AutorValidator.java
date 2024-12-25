package github.under_sin.libraryapi.validator;

import github.under_sin.libraryapi.exception.RegistroDuplicadoException;
import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void validar(Autor autor) {
        if (existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }

    private boolean existeAutorCadastrado(Autor autor) {
        Optional<Autor> autorOptional = autorRepository.findByNomeAndNascionalidadeAndDataNascimento(
                autor.getNome(), autor.getNascionalidade(), autor.getDataNascimento()
        );
        if (autor.getId() == null)
            return autorOptional.isPresent();

        return autorOptional.isPresent() && !autor.getId().equals(autorOptional.get().getId());
    }
}
