package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.CadastroLivroDTO;
import github.under_sin.libraryapi.controller.dto.RespostaPesquisaLivroDTO;
import github.under_sin.libraryapi.controller.mappers.LivroMapper;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<RespostaPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        Optional<Livro> livroOptional = service.obterPorId(id);
        if (livroOptional.isPresent()) {
            Livro livro = livroOptional.get();
            RespostaPesquisaLivroDTO resposta = RespostaPesquisaLivroDTO.criaRespostaDTO(livro);
            return ResponseEntity.ok(resposta);
        }
        return ResponseEntity.notFound().build();
    }
}
