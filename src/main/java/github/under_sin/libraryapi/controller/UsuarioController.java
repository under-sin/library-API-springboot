package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.UsuarioDTO;
import github.under_sin.libraryapi.controller.mappers.UsuarioMapper;
import github.under_sin.libraryapi.model.Usuario;
import github.under_sin.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("usuarios")
public class UsuarioController implements GenericController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    public UsuarioController(UsuarioService service, UsuarioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = mapper.toEntitiy(dto);
        service.salvar(usuario);
        URI locale = gerarHeaderLocation(usuario.getId());
        return ResponseEntity.created(locale).build();
    }
}
