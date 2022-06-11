package library.exceptions;

public class NoPageObjectAnnotation extends RuntimeException {

    public NoPageObjectAnnotation(String message) {
        super(message);
    }
}
