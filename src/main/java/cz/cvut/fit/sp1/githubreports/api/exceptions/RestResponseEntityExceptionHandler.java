package cz.cvut.fit.sp1.githubreports.api.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityStateException.class})
    public ResponseEntity<Object> handleEntityState(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Entity not unique", new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NoEntityFoundException.class})
    public ResponseEntity<Object> handleNoEntityFound(RuntimeException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "No entity found");

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IncorrectRequestException.class})
    public ResponseEntity<Object> handleIncorrectRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Request is not correct", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "User has not permissions for access", new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {HasRelationsException.class})
    public ResponseEntity<Object> handleHasRelations(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Has relations", new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
