package github.under_sin.libraryapi.controller.common;

import github.under_sin.libraryapi.controller.dto.ErroCampo;
import github.under_sin.libraryapi.controller.dto.ErroResposta;
import github.under_sin.libraryapi.exception.CampoInvalidoException;
import github.under_sin.libraryapi.exception.OperacaoNaoPermitidaException;
import github.under_sin.libraryapi.exception.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ErroCampo> erroCampoList = fieldErrors
            .stream()
            .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
            .collect(Collectors.toList());
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", erroCampoList);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta hanldeRegistroDuplicadoException(RegistroDuplicadoException ex) {
        return ErroResposta.conflito(ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException ex) {
        return ErroResposta.respostaPadrao(ex.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException ex) {
        return new ErroResposta(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Erro de validação",
            List.of(new ErroCampo(ex.getCampo(), ex.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrorNaoTratados(RuntimeException ex) {
        return new ErroResposta(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Ocorreu um erro inesperado. Entre em contato com o nosso suporte",
            List.of());
    }
}
