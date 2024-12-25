package github.under_sin.libraryapi.repository;

import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTeste() {
        // salvando uma entidade que possui relacionamento
        Livro livro = new Livro();
        livro.setIsbn("123124-1231");
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(89.99));
        livro.setTitulo("Dandadan");
        livro.setDataPublicacao(LocalDate.of(2010, 10, 11));

        Autor autor = autorRepository
            .findById(UUID.fromString("b365fd71-a2f6-4ebd-af32-b318dff822ab"))
            .orElse(null);
        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void salvarCascadeTest() {
        // Dessa forma é preciso configurar o cascade no relacionamento da entidade
        // porém, essa não é uma boa prática, a melhor seria criar o autor, salvar e depois criar o livro.

        Livro livro = new Livro();
        livro.setIsbn("123124-1231");
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(89.99));
        livro.setTitulo("Berserk");
        livro.setDataPublicacao(LocalDate.of(2010, 10, 11));

        Autor autor = new Autor();
        autor.setNome("Xandao");
        autor.setNascionalidade("brasileiro");
        autor.setDataNascimento(LocalDate.of(2001, 2, 24));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void salvarAutorDepoisLivroTest() {

        Autor autor = new Autor();
        autor.setNome("Zunzzzzun");
        autor.setNascionalidade("brasileiro");
        autor.setDataNascimento(LocalDate.of(2001, 2, 24));

        Livro livro = new Livro();
        livro.setIsbn("1231241");
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(89.99));
        livro.setTitulo("Eu sei o que vcs n fizeram");
        livro.setDataPublicacao(LocalDate.of(2010, 10, 11));

        autorRepository.save(autor);

        livro.setAutor(autor);
        livroRepository.save(livro);
    }

    @Test
    @Transactional
    void buscarLivroFetchLazyTest() {
        var livro = livroRepository.findById(UUID.fromString("f28f822c-3aba-452a-93b9-7b684d5706b0")).orElse(null);
        System.out.println("--------- LIVRO ---------");
        System.out.println("LIVRO: " + livro.getTitulo());

        System.out.println("--------- AUTOR ---------");
        System.out.println("AUTOR: " + livro.getAutor().getNome());
    }

    @Test
    void atualizarLivroTest() {
        Optional<Livro> livroQuery = livroRepository.findById(UUID.fromString("81a922ba-bc62-4f83-9c91-e39138c5d023"));

        if (livroQuery.isPresent()) {
            Livro livro = livroQuery.get();
            livro.setTitulo("Mitologia nordica");
            livroRepository.save(livro);
        }
    }

    @Test
    void listaLivrosPorTituleAndPreco() {
        String titulo = "Sandman";
        BigDecimal preco = BigDecimal.valueOf(189.99);

        var livros = livroRepository.findByTituloAndPreco(titulo, preco);
        livros.forEach(System.out::println);
    }
    
    @Test
    void listaLivrosPorTituloOuGenero() {
        var livros = livroRepository.findByTituloOrGenero("Eu sei o que vcs n fizeram", GeneroLivro.FANTASIA);
        livros.forEach(System.out::println);
    }

    @Test
    void listaLivrosOrdenandosPorTituloAndPrecoTest() {
        var livros = livroRepository.listarTodosOrdenandoPorTituloAndPreco();
        livros.forEach(System.out::println);
    }

    @Test
    @Transactional
    void listaAutoresJoinLivrosTest() {
        var autores = livroRepository.listarAutoresPorLivros();
        autores.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParam() {
        var autores = livroRepository.findByGenero(GeneroLivro.FANTASIA, "dataCriacao");
        autores.forEach(System.out::println);
    }

    @Test
    void deletaLivroPorGenero() {
        livroRepository.deleteByGenero(GeneroLivro.FICCAO);
    }

    @Test
    void atualizaDataPublicacao() {
        livroRepository.updateDataPublicacao(LocalDate.of(2000, 1, 1));
    }
}