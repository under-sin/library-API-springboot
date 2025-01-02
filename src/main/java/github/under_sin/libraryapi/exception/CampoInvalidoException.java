package github.under_sin.libraryapi.exception;

public class CampoInvalidoException extends RuntimeException {

    public CampoInvalidoException(String campo, String message) {
        super(message);
        this.campo = campo;
    }

    private final String campo;

    public String getCampo() {
        return campo;
    }
}
