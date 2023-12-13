package br.com.tutoraia.infra;

import br.com.tutoraia.resposta.ExcecaoResposta;
import br.com.tutoraia.excecao.ParametrosInvalidosExcecao;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ParametrosInvalidosExcecao.class)
    public ResponseEntity<ExcecaoResposta> handleInvalidParamsException(ParametrosInvalidosExcecao ex) {
        return ResponseEntity.badRequest().body(new ExcecaoResposta(ex.getName(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
