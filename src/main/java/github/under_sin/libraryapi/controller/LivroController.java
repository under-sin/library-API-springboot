package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.CadastroLivroDTO;
import github.under_sin.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import github.under_sin.libraryapi.controller.mappers.LivroMapper;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import github.under_sin.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        return service
            .obterPorId(id)
            .map(livro -> {
                var dto = mapper.toDTO(livro);
                return ResponseEntity.ok(dto);
            }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return service
            .obterPorId(id)
            .map(livro -> {
                service.deletar(livro);
                return ResponseEntity.noContent().build();
            }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
        @RequestParam(value = "isbn", required = false) String isbn,
        @RequestParam(value = "titulo", required = false) String titulo,
        @RequestParam(value = "nome-autor", required = false) String nomeAutor,
        @RequestParam(value = "genero", required = false) GeneroLivro genero,
        @RequestParam(value = "ano-publicacao", required = false) Integer anoPublicacao,
        @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
        @RequestParam(value = "tamanho-pagina", defaultValue = "10") Integer tamanhoPagina
    ) {
        var resultado = service.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);
        Page<ResultadoPesquisaLivroDTO> retorno = resultado.map(mapper::toDTO);
        return ResponseEntity.ok(retorno);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto) {
        return service
            .obterPorId(id)
            .map(livro -> {
                Livro entidadeAux = mapper.toEntity(dto);

                livro.setIsbn(entidadeAux.getIsbn());
                livro.setTitulo(entidadeAux.getTitulo());
                livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                livro.setGenero(entidadeAux.getGenero());
                livro.setPreco(entidadeAux.getPreco());
                livro.setAutor(entidadeAux.getAutor());

                service.atualizar(livro);
                return ResponseEntity.noContent().build();
        }).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
