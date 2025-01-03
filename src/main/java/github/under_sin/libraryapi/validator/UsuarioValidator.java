package github.under_sin.libraryapi.validator;

import github.under_sin.libraryapi.exception.RegistroDuplicadoException;
import github.under_sin.libraryapi.model.Usuario;
import github.under_sin.libraryapi.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public UsuarioValidator(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void validar(Usuario usuario) {
        if (existeLogin(usuario)) {
            throw new RegistroDuplicadoException("O login " + usuario.getLogin() + " já está cadastrado");
        }
    }

    private Boolean existeLogin(Usuario usuario) {
        return repository.existsByLogin(usuario.getLogin());
    }
}
