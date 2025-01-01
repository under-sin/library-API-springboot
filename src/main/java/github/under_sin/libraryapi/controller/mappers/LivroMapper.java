package github.under_sin.libraryapi.controller.mappers;

import github.under_sin.libraryapi.controller.dto.CadastroLivroDTO;
import github.under_sin.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import github.under_sin.libraryapi.model.Livro;
import github.under_sin.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// podemos passar um array de outros mappers par ajudar com mapeamentos já existentes
// nesse caso estamos usando o AutoMapper para mapear o autor para autorDTO
@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    // podemos passar uma expressão no mapStruct - para isso é preciso mudar de interface para abstract class
    @Mapping(target = "autor", expression = "java(autorRepository.findById(dto.idAutor()).orElse(null))")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
