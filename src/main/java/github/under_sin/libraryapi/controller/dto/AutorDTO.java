package github.under_sin.libraryapi.controller.dto;

import github.under_sin.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String nascionalidade) {

    public Autor mapeiaParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNascionalidade(this.nascionalidade);
        return autor;
    }
}