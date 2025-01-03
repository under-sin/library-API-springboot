package github.under_sin.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UsuarioDTO(
        @NotBlank
        String login,

        @NotBlank
        String senha,

        @NotEmpty
        List<String> roles) {
}
