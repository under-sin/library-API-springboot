package github.under_sin.libraryapi.controller;

import github.under_sin.libraryapi.controller.dto.AutorDTO;
import github.under_sin.libraryapi.controller.dto.ErroResposta;
import github.under_sin.libraryapi.exception.OperacaoNaoPermitidaException;
import github.under_sin.libraryapi.exception.RegistroDuplicadoException;
import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody AutorDTO autor) {
        try {
            Autor autorEntity = autor.mapeiaParaAutor();
            autorService.salvar(autorEntity);

            // vai criar a uri de retorno para localizar a entidade criada
            // http://localhost:8080/autores/{id}
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntity.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException ex) {
            ErroResposta resposta = ErroResposta.conflito(ex.getMessage());
            return ResponseEntity.status(resposta.status()).body(resposta);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody AutorDTO dto) {
        try {
            Optional<Autor> autorOptional = autorService.obterPorId(id);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Autor autor = autorOptional.get();
            autor.setNome(dto.nome());
            autor.setNascionalidade(dto.nascionalidade());
            autor.setDataNascimento(dto.dataNascimento());
            autorService.atualizar(autor);
            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException ex) {
            ErroResposta resposta = ErroResposta.conflito(ex.getMessage());
            return ResponseEntity.status(resposta.status()).body(resposta);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String idAutor) {
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if(autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNascionalidade());
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String idAutor) {
        try {
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
            if (autorOptional.isEmpty())
                return ResponseEntity.notFound().build();

            Autor autor = autorOptional.get();
            autorService.deletar(autor);
            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaException ex) {
            ErroResposta erroResposta = ErroResposta.respostaPadrao(ex.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nascionalidade", required = false) String nascionalidade) {
        List<Autor> autorResponse = autorService.pesquisar(nome, nascionalidade);
        List<AutorDTO> autor = autorResponse
                .stream()
                .map(a -> new AutorDTO(
                    a.getId(),
                    a.getNome(),
                    a.getDataNascimento(),
                    a.getNascionalidade())
                ).collect(Collectors.toList());
        return ResponseEntity.ok(autor);
    }
}
