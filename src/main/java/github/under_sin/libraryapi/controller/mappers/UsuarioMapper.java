package github.under_sin.libraryapi.controller.mappers;

import github.under_sin.libraryapi.controller.dto.UsuarioDTO;
import github.under_sin.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntitiy(UsuarioDTO dto);
}
