package github.under_sin.libraryapi.controller.mappers;

import github.under_sin.libraryapi.controller.dto.AutorDTO;
import github.under_sin.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);
    AutorDTO toDTO(Autor entity);
}
