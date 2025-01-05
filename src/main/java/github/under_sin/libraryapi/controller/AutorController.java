package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.AutorDTO;
import github.under_sin.libraryapi.controller.mappers.AutorMapper;
import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
public class AutorController implements GenericController {

    @Autowired
    private AutorService autorService;

    @Autowired
    private AutorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto) {
        Autor autor = mapper.toEntity(dto);
        autorService.salvar(autor);
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizar(@PathVariable("id") String id, @RequestBody @Valid AutorDTO dto) {
        Optional<Autor> autorOptional = autorService.obterPorId(id);
        if (autorOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Autor autor = autorOptional.get();
        autor.setNome(dto.nome());
        autor.setNascionalidade(dto.nascionalidade());
        autor.setDataNascimento(dto.dataNascimento());
        autorService.atualizar(autor);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRoles('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String idAutor) {
        // podemos usar essa sintax quando trabalhamos com optinal
        return autorService
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> deletar(@PathVariable("id") String idAutor) {
        return autorService
                .obterPorId(idAutor)
                .map(autor -> {
                    autorService.deletar(autor);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRoles('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nascionalidade", required = false) String nascionalidade) {
        List<Autor> autorResponse = autorService.pesquisaByExample(nome, nascionalidade);
        List<AutorDTO> autor = autorResponse
                .stream()
                .map(mapper::toDTO) // como o .map tem o autor como referencia, podemos passar o mapper dessa forma
                .collect(Collectors.toList());
        return ResponseEntity.ok(autor);
    }
}
