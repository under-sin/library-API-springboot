package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.AutorDTO;
import github.under_sin.libraryapi.controller.mappers.AutorMapper;
import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto) {
        Autor autor = mapper.toEntity(dto);
        autorService.salvar(autor);
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid AutorDTO dto) {
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
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String idAutor) {
        // podemos usar essa sintax quando trabalhamos com optinal
        return autorService
            .obterPorId(idAutor)
            .map(autor -> {
                AutorDTO dto = mapper.toDTO(autor);
                return ResponseEntity.ok(dto);
            }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String idAutor) {
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Autor autor = autorOptional.get();
        autorService.deletar(autor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
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
