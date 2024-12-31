package github.under_sin.libraryapi.controller.dto;

import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RespostaPesquisaLivroDTO(
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        AutorDTO autor
) {
    public static RespostaPesquisaLivroDTO criaRespostaDTO(Livro livro) {
        AutorDTO autor = new AutorDTO(
                livro.getAutor().getId(),
                livro.getAutor().getNome(),
                livro.getAutor().getDataNascimento(),
                livro.getAutor().getNascionalidade()
        );

        return new RespostaPesquisaLivroDTO(
                livro.getId(),
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getDataPublicacao(),
                livro.getGenero(),
                livro.getPreco(),
                autor
        );
    }
}
