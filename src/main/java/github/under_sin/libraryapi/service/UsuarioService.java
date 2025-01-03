package github.under_sin.libraryapi.service;

import github.under_sin.libraryapi.model.Usuario;
import github.under_sin.libraryapi.repository.UsuarioRepository;
import github.under_sin.libraryapi.validator.UsuarioValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final UsuarioValidator validator;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder, UsuarioValidator validator) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
    }

    public void salvar(Usuario usuario) {
        validator.validar(usuario);
        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        repository.save(usuario);
    }

    public Optional<Usuario> obterPorLogin(String login) {
        return repository.findByLogin(login);
    }
}
