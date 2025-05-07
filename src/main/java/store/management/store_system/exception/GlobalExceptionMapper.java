package store.management.store_system.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import store.management.store_system.dto.AppResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionMapper extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String responseDescription = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        AppResponse<String> response = new AppResponse<>(-1, responseDescription);
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement (NoSuchElementException noSuchElementException){
        return new ResponseEntity<String>("NO VALUE IS PRESENT IN THE DB", HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>("PLEASE CHANGE HTTP RETURN TYPE", HttpStatus.NOT_FOUND);
    }

}
