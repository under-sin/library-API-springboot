package github.under_sin.libraryapi.repository;

import github.under_sin.libraryapi.model.Autor;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.model.enums.GeneroLivro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("JRR Tolken");
        autor.setNascionalidade("brasileiro");
        autor.setDataNascimento(LocalDate.of(2001, 2, 24));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor salvo: " + autorSalvo.toString());
    }

    @Test
    public void atualizarAutorTest() {
        var id = UUID.fromString("ca01441c-e232-437a-b93f-f7d47d6264d9");
        Optional<Autor> optionalAutor = repository.findById(id);

        if (optionalAutor.isPresent()) {
            Autor autor = optionalAutor.get();
            autor.setNascionalidade("Argentino");

            autor = repository.save(autor);
            System.out.println("atualizando autor " + autor.toString());
        }
    }

    @Test
    public void listarAutorTest() {
        List<Autor> listAutor = repository.findAll();
        listAutor.forEach(System.out::println);
    }

    @Test
    public void countAutorTest() {
        System.out.println("Contagem de autores " + repository.count());
    }

    @Test
    public void deletarAutorPorIdTest() {
        var id = UUID.fromString("c194d764-475c-444f-b931-2134a68634bb");
        repository.deleteById(id);
    }

    @Test
    void deleteAutorObjectTest() {
        var id = UUID.fromString("dcc3fe75-ffc7-4df6-9af0-ab79d62de4f8");
        var autor = repository.findById(id).get();
        repository.delete(autor);
    }

    @Test
    void salvaAutorComListaDeLivroTest() {
        // salvando o autor com uma lista de livros sem usar o cascade
        Autor autor = new Autor();
        autor.setNome("Neil Gaiman");
        autor.setNascionalidade("Estadunidence");
        autor.setDataNascimento(LocalDate.of(1978, 2, 24));

        Livro livro = new Livro();
        livro.setIsbn("99999-1231");
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setPreco(BigDecimal.valueOf(189.99));
        livro.setTitulo("Sandman");
        livro.setDataPublicacao(LocalDate.of(2010, 10, 11));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("99999-1231");
        livro2.setGenero(GeneroLivro.FANTASIA);
        livro2.setPreco(BigDecimal.valueOf(189.99));
        livro2.setTitulo("Deuses Americanos");
        livro2.setDataPublicacao(LocalDate.of(2010, 10, 11));
        livro2.setAutor(autor);

        var livroLista = new ArrayList<Livro>();
        livroLista.add(livro);
        livroLista.add(livro2);
        autor.setLivros(livroLista);

        repository.save(autor);
        livroRepository.saveAll(autor.getLivros());
    }

    @Test
    void salvaAutorComListaDeLivroComCascadeTest() {
        // salvando o autor com uma lista de livros sem usar o cascade
        Autor autor = new Autor();
        autor.setNome("Neil Gaiman");
        autor.setNascionalidade("Estadunidence");
        autor.setDataNascimento(LocalDate.of(1978, 2, 24));

        Livro livro = new Livro();
        livro.setIsbn("99999-1231");
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setPreco(BigDecimal.valueOf(189.99));
        livro.setTitulo("Sandman");
        livro.setDataPublicacao(LocalDate.of(2010, 10, 11));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("99999-1231");
        livro2.setGenero(GeneroLivro.FANTASIA);
        livro2.setPreco(BigDecimal.valueOf(189.99));
        livro2.setTitulo("Deuses Americanos");
        livro2.setDataPublicacao(LocalDate.of(2010, 10, 11));
        livro2.setAutor(autor);

        var livroLista = new ArrayList<Livro>();
        livroLista.add(livro);
        livroLista.add(livro2);
        autor.setLivros(livroLista);

        repository.save(autor);
    }


    @Test
    void listarLivrosAutor() {
        var id = UUID.fromString("d6f2c8ec-bf46-43af-bf98-4771c0ec5e08");
        Autor autor = repository.findById(id).get();

        List<Livro> livros = livroRepository.findByAutor(autor);
        autor.setLivros(livros);

        autor.getLivros().forEach(System.out::println);
    }
}
