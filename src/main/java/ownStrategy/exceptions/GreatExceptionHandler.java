package ownStrategy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GreatExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();
        for(FieldError error : allErrors){
            errors.put(error.getField(),error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(TickerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // To zamieni 500 na 404 w Twoim .http
    public Map<String, String> tickerExceptions(TickerNotFoundException ex){
    Map<String,String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("code", "TICKER_NOT_FOUND");
        return errors;
    }

    @ExceptionHandler(KeyWordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> keyWordExceptions(KeyWordException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("code", "KEY_WORD_TOO_SHORT");
        return errors;
    }
}
