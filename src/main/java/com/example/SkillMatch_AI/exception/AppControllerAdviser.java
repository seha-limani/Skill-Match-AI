package com.example.SkillMatch_AI.exception;

import com.example.SkillMatch_AI.controller.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppControllerAdviser {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(RuntimeException exception) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setStatus("404");

        return errorResponse;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return errors;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage("Unexpected error: " + exception.getMessage());
        errorResponse.setStatus("500");

        return errorResponse;
    }
}