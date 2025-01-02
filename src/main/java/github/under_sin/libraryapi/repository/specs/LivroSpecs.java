package github.under_sin.libraryapi.repository.specs;

import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn) {
        return ((root, query, cb)
                -> cb.equal(root.get("isbn"), isbn));
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return ((root, query, cb)
                -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%"));
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero) {
        return ((root, query, cb)
                -> cb.equal(root.get("genero"), genero));
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao) {
        return ((root, query, cb)
            -> cb.equal(
                // select to_char(data_publicacao, 'YYYY') from livro;
                cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY")),
                anoPublicacao.toString())
        );
    }

    public static Specification<Livro> nomeAutorLike(String nomeAutor) {
        // forma de usar um join usando a specification
        return ((root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER);
            return cb.like( cb.upper(joinAutor.get("nome")), "%" + nomeAutor.toUpperCase() + "%");
        });
    }
}
