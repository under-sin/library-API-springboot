package github.under_sin.libraryapi.controller.dto;

import github.under_sin.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,

        @NotBlank(message = "Campo obrigatório")
        @Size(max = 100, min = 2, message = "Campo fora do tamanho permitido")
        String nome,

        @NotNull(message = "Campo obrigatório")
        @Past(message = "Não é permitido uma data futura a atual para este campo")
        LocalDate dataNascimento,

        @NotBlank(message = "Campo obrigatório")
        @Size(max = 100, min = 2, message = "Campo fora do tamanho permitido")
        String nascionalidade) {

    public Autor mapeiaParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNascionalidade(this.nascionalidade);
        return autor;
    }
}