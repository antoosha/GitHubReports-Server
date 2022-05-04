package cz.cvut.fit.sp1.githubreports.api.exceptions;

public class IncorrectRequestException extends Exception {
    public IncorrectRequestException() {
        super("Bad request");
    }
}
