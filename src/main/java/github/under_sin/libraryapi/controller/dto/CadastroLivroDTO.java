package github.under_sin.libraryapi.controller.dto;

import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(

        @ISBN
        @NotBlank(message = "Campo obrigatório")
        String isbn,

        @NotBlank(message = "Campo obrigatório")
        String titulo,

        @Past(message = "O campo não pode ter uma data maior que a atual")
        @NotNull(message = "Campo obrigatório")
        LocalDate dataPublicacao,

        GeneroLivro genero,

        BigDecimal preco,

        @NotNull(message = "Campo obrigatório")
        UUID idAutor
) {
    public Livro mapeiaParaLivro() {
        Livro livro = new Livro();
        livro.setTitulo(this.titulo);
        livro.setIsbn(this.isbn);
        livro.setDataPublicacao(this.dataPublicacao);
        livro.setGenero(this.genero);
        livro.setPreco(this.preco);
        return livro;
    }
}
