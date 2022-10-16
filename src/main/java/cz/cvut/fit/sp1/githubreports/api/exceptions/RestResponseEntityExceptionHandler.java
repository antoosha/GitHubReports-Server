package cz.cvut.fit.sp1.githubreports.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityStateException.class})
    public ResponseEntity<ExceptionDTO> handleEntityState(EntityStateException ex) {
        return status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(EntityStateException.class.getSimpleName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {NoEntityFoundException.class})
    public ResponseEntity<ExceptionDTO> handleNoEntityFound(NoEntityFoundException ex) {
        return status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(NoEntityFoundException.class.getSimpleName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {IncorrectRequestException.class})
    public ResponseEntity<ExceptionDTO> handleIncorrectRequest(IncorrectRequestException ex) {
        return status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(IncorrectRequestException.class.getSimpleName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ExceptionDTO> handleAccessDenied(AccessDeniedException ex) {
        return status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(AccessDeniedException.class.getSimpleName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {HasRelationsException.class})
    public ResponseEntity<ExceptionDTO> handleHasRelations(HasRelationsException ex) {
        return status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(HasRelationsException.class.getSimpleName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionDTO> handleUnknownException(Exception ex) {
        return status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new ExceptionDTO(RuntimeException.class.getSimpleName(), ex.getMessage()));
    }
}
