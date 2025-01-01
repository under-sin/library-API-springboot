package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.CadastroLivroDTO;
import github.under_sin.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import github.under_sin.libraryapi.controller.mappers.LivroMapper;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("livros")
public class LivroController implements GenericController {

    private final LivroService service;
    private final LivroMapper mapper;

    public LivroController(LivroService service, LivroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        return service
            .obterPorId(id)
            .map(livro -> {
                var dto = mapper.toDTO(livro);
                return ResponseEntity.ok(dto);
            }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return service
            .obterPorId(id)
            .map(livro -> {
                service.deletar(livro);
                return ResponseEntity.noContent().build();
            }).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
